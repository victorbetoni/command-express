package net.threadly.commandapi;

import net.threadly.commandapi.args.CommandContext;
import net.threadly.commandapi.args.CommandElement;
import net.threadly.commandapi.exception.CastNotPossibleException;
import net.threadly.commandapi.result.CommandResult;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
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
        Objects.requireNonNull(plugin.getCommand(entryPoint.getAlias())).setExecutor((sender, command, label, args) -> {

            CommandSpec commandSpec = entryPoint;

            ListIterator<String> commandPathIterator = Arrays.asList(args).listIterator();
            ArrayList<CommandElement> commandSpectedArguments = new ArrayList<>();

            Map<String, Object> arguments = new HashMap<>();

            if(commandPathIterator.hasNext()) {
                StringBuilder pathBuilder = new StringBuilder();
                pathBuilder.append(entryPoint.getAlias()).append(" ");
                pathBuilder.append(commandPathIterator.next());

                do {
                    commandSpec = getSpecByPath(pathBuilder.toString().trim()).get();
                    if(commandPathIterator.hasNext()) pathBuilder.append(commandPathIterator.next()).append(" ");
                }while(getSpecByPath(pathBuilder.toString().trim()).isPresent() && commandPathIterator.hasNext());

                commandPathIterator.previous();

                commandSpec.getArguments().ifPresent((commandElements) -> {
                    commandSpectedArguments.addAll(Arrays.asList(commandElements));
                });

                List<String> passedArguments = new ArrayList<>();
                commandPathIterator.forEachRemaining(passedArguments::add);

                if(passedArguments.size() < commandSpectedArguments.size()) {
                    sender.sendMessage(getCorrectUsageText(commandSpec, pathBuilder.toString()));
                    return true;
                }

                try {
                    arguments = populateArguments(passedArguments, commandSpectedArguments);
                } catch (CastNotPossibleException e) {
                    sender.sendMessage(getCorrectUsageText(commandSpec, pathBuilder.toString()));
                    return true;
                }

            }

            CommandContext context = new CommandContext(arguments, sender, System.currentTimeMillis()/1000);

            if(commandSpec.isPlayerOnly() && !(sender instanceof Player)) {
                sender.sendMessage("§cCommand for player only.");
                return true;
            }

            if(commandSpec.getPermission().isPresent() && !sender.hasPermission(commandSpec.getPermission().get())) {
                sender.sendMessage("§cYou are not allowed to perform this command.");
                return true;
            }

            CommandResult cmdResult = commandSpec.getExecutor().execute(context);

            cmdResult.getResult().ifPresent(result -> {
                cmdResult.getMessage().ifPresent(message -> {
                    switch(result){
                        case NONE:
                            sender.sendMessage(message);
                            break;
                        case FAILURE:
                            sender.sendMessage(ChatColor.RED + message);
                            if(sender instanceof Player){
                                Player p = (Player) sender;
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                            }
                            break;
                        case SUCCESS:
                            sender.sendMessage(ChatColor.GREEN + message);
                            if(sender instanceof Player){
                                Player p = (Player) sender;
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                            }
                            break;
                    }
                });
            });

            return false;
        });
    }

    public static Optional<CommandSpec> getSpecByPath(String path) {
        return registry.stream().filter(commandSpec -> findPath(commandSpec).equalsIgnoreCase(path)).findFirst();
    }

    public static String findPath(CommandSpec spec){
        if(!spec.getBelonger().isPresent()) return spec.getAlias();

        StringBuilder path = new StringBuilder();
        CommandSpec belongsTo = spec;
        path.append(spec.getAlias()).append(" ");

        while(belongsTo.getBelonger().isPresent()) {
            belongsTo = belongsTo.getBelonger().get();
            path.append(belongsTo.getAlias()).append(" ");
        }

        List<String> pathArgs = Arrays.asList(path.toString().trim().split(" "));
        Collections.reverse(pathArgs);

        return String.join(" ", pathArgs);
    }

    private static String getCorrectUsageText(CommandSpec spec, String path) {
        if(spec.getArguments().isPresent()) {
            StringBuilder correctUsage = new StringBuilder();
            correctUsage.append("§cCorrect usage: ").append(path).append(" ");
            Arrays.asList(spec.getArguments().get())
                    .forEach(arg -> correctUsage.append("§c<").append(arg.getKey()).append("§c>").append(" "));
            return correctUsage.toString();
        }
        return "§cCorrect usage: " + path;
    }

    private static Map<String, Object> populateArguments(List<String> passedArgs, List<CommandElement> spectedArguments) throws CastNotPossibleException {
        Map<String, Object> args = new HashMap<>();
        Iterator<String> passedArgsIterator = passedArgs.iterator();
        Iterator<CommandElement> spectedArgumentsIterator = spectedArguments.iterator();

        while (passedArgsIterator.hasNext()) {
            String passedArg = passedArgsIterator.next();
            if(spectedArgumentsIterator.hasNext()) {
                CommandElement spectedArgument = spectedArgumentsIterator.next();
                if(spectedArgument.isJoinString()) {
                    StringBuilder passedText = new StringBuilder();
                    passedText.append(passedArg).append(" ");
                    passedArgsIterator.forEachRemaining(x -> passedText.append(x).append(" "));
                    args.put(spectedArgument.getKey(), passedText.toString().trim());
                }else{
                    args.put(spectedArgument.getKey(), spectedArgument.cast(passedArg));
                }
            }
        }

        return args;
    }
}

