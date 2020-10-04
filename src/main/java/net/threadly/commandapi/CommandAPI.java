package net.threadly.commandapi;

import net.threadly.commandapi.Result;
import net.threadly.commandapi.args.CommandElement;
import net.threadly.commandapi.args.GenericArguments;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class CommandAPI extends JavaPlugin {
    private static Set<CommandSpec> registry;

    @Override
    public void onEnable() {
        this.registry = new HashSet<>();
    }

    public static void register(CommandSpec spec){
        registry.add(spec);
    }

    public static void registerCommandEntryPoint(JavaPlugin plugin, final CommandSpec entryPoint){
        plugin.getCommand(entryPoint.getAlias()).setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
                ListIterator<String> argsIterator = Arrays.asList(strings).listIterator();
                CommandSpec spec = entryPoint;
                Map<String, String> args = new HashMap<>();
                ListIterator<String> specArgumentsIterator = spec.getArguments().listIterator();
                StringBuilder path = new StringBuilder();
                path.append(spec.getAlias());

                CommandElement<OfflinePlayer> str = GenericArguments.offlinePlayer("bruh");
                String rfs = str.cast("bruh", "bruh");

                if(argsIterator.hasNext()){
                    path.append(argsIterator.next());;

                    while(getSpecByPath(path.toString().trim()) != null){
                        path.append(argsIterator.next());
                        spec = getSpecByPath(path.toString().trim());
                    }
                    specArgumentsIterator = spec.getArguments().listIterator();
                    String text = "";
                    String previousArg = "";
                    while(argsIterator.hasNext()) {
                        if(!specArgumentsIterator.hasNext()){
                            if(text.equalsIgnoreCase("")){
                                text += argsIterator.previous();
                            }
                            if(previousArg.equalsIgnoreCase("")){
                                previousArg = specArgumentsIterator.previous();
                            }
                            text+=argsIterator.next() + " ";
                        }
                        if(specArgumentsIterator.hasNext()){
                            args.put(specArgumentsIterator.next(), argsIterator.next());
                        }
                    }
                    args.remove(previousArg);
                    args.put(previousArg, text.trim());
                }


                if(specArgumentsIterator.hasNext()) {
                    final StringBuilder correctUsage = new StringBuilder();
                    correctUsage.append("§cUso correto: " + path);
                    spec.getArguments().forEach(str -> correctUsage.append("<").append(str).append("> "));
                }

                if(spec.isPlayerOnly()){
                    if(!(commandSender instanceof Player)){
                        commandSender.sendMessage("§cEsse comando é apenas para jogadores.");
                    }
                }

                spec.getPermission().ifPresent(perm -> {
                    if(!commandSender.hasPermission(perm)) {
                        commandSender.sendMessage("§cVocê não tem permissão para utilizar esse comando.");
                    }
                });

                CommandResult result = spec.getExecutor().execute(commandSender, args);
                if(result.getResult() != Result.NONE){
                    switch (result.getResult()){
                        case SUCCESS:
                            if(commandSender instanceof Player){
                                Player p = (Player) commandSender;
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                            }
                            break;
                        case FAILURE:
                            if(commandSender instanceof Player){
                                Player p = (Player) commandSender;
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                            }
                    }
                }

                if(result.getMessage() != null){
                    switch (result.getResult()){
                        case SUCCESS:
                            commandSender.sendMessage("§a" + result.getMessage());
                            break;
                        case FAILURE:
                            commandSender.sendMessage("§c" + result.getMessage());
                            break;
                        case NONE:
                            commandSender.sendMessage(result.getMessage());
                            break;
                    }
                }

                return false;
            }
        });
    }

    public static CommandSpec getSpecByPath(String path) {
        for(CommandSpec spec : registry){
            if(findPath(spec).equalsIgnoreCase(path)){
                return spec;
            }
        }
        return null;
    }

    public static String findPath(CommandSpec spec){
        List<String> path = new ArrayList<>();
        path.add(spec.getAlias());
        spec.getBelongsTo().ifPresent(spc -> {
            Optional<CommandSpec> belonger = spc.getBelongsTo();
            path.add(belonger.get().getAlias());
            while(belonger.isPresent()){
                belonger = belonger.get().getBelongsTo();
                if(belonger.isPresent()){
                    path.add(belonger.get().getAlias());
                }
            }
        });
        Collections.reverse(path);
        String pathString = "";
        for(String str : path){
            pathString += str + " ";
        }
        return pathString.trim();
    }


}

