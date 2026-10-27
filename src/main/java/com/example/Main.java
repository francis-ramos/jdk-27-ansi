package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Main {
    static final HashMap<String, Integer> ansiCodes = new HashMap<>();
    static final Path mapFile = Path.of("hash.txt");
    static final Path setFile = Path.of("set.txt");

    static {
        String[] colors = {"black", "red", "green", "yellow", "blue", "magenta", "cyan", "white"};
        for (int i = 0; i < colors.length; i++)
        {
            ansiCodes.put(colors[i], 30 + i);
            ansiCodes.put("bright-" + colors[i], 90 + i);
            ansiCodes.put("bg-" + colors[i], 100 + i);
            ansiCodes.put("bg-bright" + colors[i], 40 + i);
        }
        ansiCodes.put("reset", 0);
        ansiCodes.put("bold", 1);
        ansiCodes.put("dim", 2);
        ansiCodes.put("italic", 3);
        ansiCodes.put("underline", 4);
        ansiCodes.put("blink", 5);
        ansiCodes.put("strikethrough", 9);
    }

    public static void main(String[] args) throws IOException
    {
        if (args.length < 1)
        {
            System.out.println("░█████╗░███╗░░██╗░██████╗██╗\n██╔══██╗████╗░██║██╔════╝██║\n███████║██╔██╗██║╚█████╗░██║\n██╔══██║██║╚████║░╚═══██╗██║\n██║░░██║██║░╚███║██████╔╝██║\n╚═╝░░╚═╝╚═╝░░╚══╝╚═════╝░╚═╝");
            System.out.println("\u001B[33mCOMMANDS:\u001B[0m");
            System.out.println(" ".repeat(10) + "--ansi-[\u001B[2mcolor\u001B[0m] [\u001B[3m\u001B[2m...messages\u001B[0m]");
            System.out.println(" ".repeat(10) + "--hash [\u001B[2madd | remove | list | get\u001B[0m] [\u001B[3m\u001B[2m...options\u001B[0m]");
            System.out.println(" ".repeat(10) + "--set [\u001B[2madd | remove | list\u001B[0m] [\u001B[3m\u001B[2m...options\u001B[0m]");
            System.out.println(" ".repeat(10) + "--string [\u001B[2muppercase | lowercase | reverse...\u001B[0m] [\u001B[3m\u001B[2m...options\u001B[0m]");
            System.out.println(" ".repeat(10) + "--math [\u001B[2maddition | subtraction...\u001B[0m] [\u001B[3m\u001B[2mvalue 1\u001B[0m] [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
            System.out.println(" ".repeat(10) + "--uuid [\u001B[2mcharacter length\u001B[0m]");
            return;
        }

        String command = args[0];
        String[] rest = Arrays.copyOfRange(args, 1, args.length);

        if (command.startsWith("--ansi-"))
        {
            runAnsi(command.substring("--ansi-".length()), rest);
        }
        else
        {

            switch(command)
            {
                case "--hash" -> runHash(rest);
                case "--set" -> runSet(rest);
                case "--string" -> runString(rest);
                case "--math" -> runMath(rest);
                case "--uuid" -> runUuid(rest);
                default -> {
                    System.out.println("░█████╗░███╗░░██╗░██████╗██╗\n██╔══██╗████╗░██║██╔════╝██║\n███████║██╔██╗██║╚█████╗░██║\n██╔══██║██║╚████║░╚═══██╗██║\n██║░░██║██║░╚███║██████╔╝██║\n╚═╝░░╚═╝╚═╝░░╚══╝╚═════╝░╚═╝");
                    System.out.println("\u001B[33mCOMMANDS:\u001B[0m");
                    System.out.println(" ".repeat(10) + "--ansi-[\u001B[2mcolor\u001B[0m] [\u001B[3m\u001B[2m...messages\u001B[0m]");
                    System.out.println(" ".repeat(10) + "--hash [\u001B[2madd | remove | list | get\u001B[0m] [\u001B[3m\u001B[2m...options\u001B[0m]");
                    System.out.println(" ".repeat(10) + "--set [\u001B[2madd | remove | list\u001B[0m] [\u001B[3m\u001B[2m...options\u001B[0m]");
                    System.out.println(" ".repeat(10) + "--string [\u001B[2muppercase | lowercase | reverse...\u001B[0m] [\u001B[3m\u001B[2m...options\u001B[0m]");
                    System.out.println(" ".repeat(10) + "--math [\u001B[2maddition | subtraction...\u001B[0m] [\u001B[3m\u001B[2mvalue 1\u001B[0m] [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
                    System.out.println(" ".repeat(10) + "--uuid [\u001B[2mcharacter length\u001B[0m]");
                    return;
                }
            }

        }
    }

    static void ErrorLog(String msg, String extra)
    {
        String reset = runSequence("reset");
        String bgRed = runSequence("bg-red");
        String white = runSequence("white");

        System.out.println(bgRed + white + "ERROR" + reset + " " + msg + extra);
    }

    static void runUuid(String[] rest)
    {
        String reset = runSequence("reset");
        String yellow = runSequence("yellow");
        if (rest.length < 1)
        {
            ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "METHODS:" + reset);
            System.out.println(" ".repeat(15) + "--uuid" + " [\u001B[3m\u001B[2mlength\u001B[0m]");
            return;
        }
        try
        {
            int trueLength = Integer.parseInt(rest[0]);
            if (trueLength < 1)
            {
                ErrorLog("Minimum of 1", "");
                return;
            }

            String lettersLowercase = "ABCDEFGHIJKLM%NOPQRSTUVWXYZ-abcdefghijklmnopqrstuvwxyz0123456789";
            int targetLength = trueLength;
            String randomString = "";

            while (randomString.length() < targetLength)
            {
                String[] sep = lettersLowercase.split("");
                String random = "" + Math.random() * lettersLowercase.length();
                String wholeNum = random.substring(0, (random.indexOf(".")));
                int wholeFinalNum = Integer.parseInt(wholeNum);
                randomString += sep[wholeFinalNum];
            }

            System.out.println(randomString);
        }
        catch (Exception e)
        {
            ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "USAGE:" + reset);
            System.out.println(" ".repeat(15) + "--uuid" + " [\u001B[3m\u001B[2mlength\u001B[0m]");
            return;
        }
    }

    static void runMath(String[] rest)
    {
        String dim = runSequence("dim");
        String reset = runSequence("reset");
        String yellow = runSequence("yellow");
        if (rest.length < 1)
        {
            ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "METHODS:" + reset);
            System.out.println(" ".repeat(15) + "--math" + dim + " addition " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
            System.out.println(" ".repeat(15) + "--math" + dim + " subtraction " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
            System.out.println(" ".repeat(15) + "--math" + dim + " multiplication " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
            System.out.println(" ".repeat(15) + "--math" + dim + " division " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
            System.out.println(" ".repeat(15) + "--math" + dim + " modulo " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
            System.out.println(" ".repeat(15) + "--math" + dim + " power " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
            return;
        }
        String method = rest[0];
        
        try
        {
            switch (method)
            {
                case "addition" -> {
                    if (rest.length < 3)
                    {
                        ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "USAGE:" + reset);
                        System.out.println(" ".repeat(15) + "--math" + dim + " addition " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
                        return;
                    }
                    int value1 = Integer.parseInt(rest[1]);
                    int value2 = Integer.parseInt(rest[2]);
                    System.out.println(value1 + value2);
                }
                case "subtraction" -> {
                    if (rest.length < 3)
                    {
                        ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "USAGE:" + reset);
                        System.out.println(" ".repeat(15) + "--math" + dim + " subtraction " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
                        return;
                    }
                    int value1 = Integer.parseInt(rest[1]);
                    int value2 = Integer.parseInt(rest[2]);
                    System.out.println(value1 - value2);
                }
                case "multiplication" -> {
                    if (rest.length < 3)
                    {
                        ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "USAGE:" + reset);
                        System.out.println(" ".repeat(15) + "--math" + dim + " multiplication " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
                        return;
                    }
                    int value1 = Integer.parseInt(rest[1]);
                    int value2 = Integer.parseInt(rest[2]);
                    System.out.println(value1 * value2);
                }
                case "division" -> {
                    if (rest.length < 3)
                    {
                        ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "USAGE:" + reset);
                        System.out.println(" ".repeat(15) + "--math" + dim + " division " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
                        return;
                    }
                    int value1 = Integer.parseInt(rest[1]);
                    int value2 = Integer.parseInt(rest[2]);
                    if (value1 == 0 || value2 == 0)
                    {
                        System.out.println("Undefine");
                    }
                    else
                    {
                        System.out.println(value1 / value2);
                    }
                }
                case "modulo" -> {
                    if (rest.length < 3)
                    {
                        ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "USAGE:" + reset);
                        System.out.println(" ".repeat(15) + "--math" + dim + " modulo " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
                        return;
                    }
                    int value1 = Integer.parseInt(rest[1]);
                    int value2 = Integer.parseInt(rest[2]);
                    System.out.println(value1 % value2);
                }
                case "power" -> {
                    if (rest.length < 3)
                    {
                        ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "USAGE:" + reset);
                        System.out.println(" ".repeat(15) + "--math" + dim + " power " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
                        return;
                    }
                    int value1 = Integer.parseInt(rest[1]);
                    int value2 = Integer.parseInt(rest[2]);
                    int result = value1;
                    for (int i = 0; i < (value2 - 1); i++)
                    {
                        result *= value1;
                    }
                    System.out.println(result);
                }
                default -> {
                    ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "METHODS:" + reset);
                    System.out.println(" ".repeat(15) + "--math" + dim + " addition " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
                    System.out.println(" ".repeat(15) + "--math" + dim + " subtraction " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
                    System.out.println(" ".repeat(15) + "--math" + dim + " multiplication " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
                    System.out.println(" ".repeat(15) + "--math" + dim + " division " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
                    System.out.println(" ".repeat(15) + "--math" + dim + " modulo " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
                    System.out.println(" ".repeat(15) + "--math" + dim + " power " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
                    return;
                }
            }
        }
        catch (Exception e)
        {
            ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "METHODS:" + reset);
            System.out.println(" ".repeat(15) + "--math" + dim + " addition " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
            System.out.println(" ".repeat(15) + "--math" + dim + " subtraction " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
            System.out.println(" ".repeat(15) + "--math" + dim + " multiplication " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
            System.out.println(" ".repeat(15) + "--math" + dim + " division " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
            System.out.println(" ".repeat(15) + "--math" + dim + " modulo " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
            System.out.println(" ".repeat(15) + "--math" + dim + " power " + reset + "[\u001B[3m\u001B[2mvalue 1\u001B[0m]" + " [\u001B[3m\u001B[2mvalue 2\u001B[0m]");
            return;
        }
    }

    static void runString(String[] rest)
    {
        String dim = runSequence("dim");
        String reset = runSequence("reset");
        String yellow = runSequence("yellow");
        if (rest.length < 1)
        {
            ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "METHODS:" + reset);
            System.out.println(" ".repeat(15) + "--string" + dim + " uppercase " + reset + "[\u001B[3m\u001B[2m...message\u001B[0m]");
            System.out.println(" ".repeat(15) + "--string" + dim + " lowercase " + reset + "[\u001B[3m\u001B[2m...message\u001B[0m]");
            System.out.println(" ".repeat(15) + "--string" + dim + " reverse " + reset + "[\u001B[3m\u001B[2m...message\u001B[0m]");
            System.out.println(" ".repeat(15) + "--string" + dim + " repeat " + reset + "[\u001B[3m\u001B[2mamount\u001B[0m]"  + " [\u001B[3m\u001B[2m...message\u001B[0m]");
            System.out.println(" ".repeat(15) + "--string" + dim + " capitalize " + reset + "[\u001B[3m\u001B[2m...message\u001B[0m]");
            return;
        }
        String method = rest[0];
        String body = String.join(" ", Arrays.copyOfRange(rest, 1, rest.length));

        switch (method)
        {
            case "uppercase" -> {
                if (rest.length < 2)
                {
                    ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "USAGE:" + reset);
                    System.out.println(" ".repeat(15) + "--string" + dim + " uppercase " + reset + "[\u001B[3m\u001B[2m...message\u001B[0m]");
                    return;
                }
                System.out.println(body.toUpperCase());   
            }
            case "lowercase" -> {
                if (rest.length < 2)
                {
                    ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "USAGE:" + reset);
                    System.out.println(" ".repeat(15) + "--string" + dim + " lowercase " + reset + "[\u001B[3m\u001B[2m...message\u001B[0m]");
                    return;
                }
                System.out.println(body.toLowerCase());
            }
            case "reverse" -> {
                if (rest.length < 2)
                {
                    ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "USAGE:" + reset);
                    System.out.println(" ".repeat(15) + "--string" + dim + " reverse " + reset + "[\u001B[3m\u001B[2m...message\u001B[0m]");
                    return;
                }
                String[] splittedBody = body.split("");
                String finalBody = "";

                for (int i = 0; i < splittedBody.length; i++)
                {
                    finalBody += splittedBody[(splittedBody.length - 1) - i];
                }

                System.out.println(finalBody);
            }
            case "repeat" -> {
                if (rest.length < 3)
                {
                    ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "USAGE:" + reset);
                    System.out.println(" ".repeat(15) + "--string" + dim + " repeat " + reset + "[\u001B[3m\u001B[2mamount\u001B[0m]"  + " [\u001B[3m\u001B[2m...message\u001B[0m]");
                    return;
                }

                try
                {
                    int amount = Integer.parseInt(rest[1]);
                    String repeatBody = String.join(" ", Arrays.copyOfRange(rest, 2, rest.length));
                    System.out.println(repeatBody.repeat(amount));
                }
                catch (Exception e)
                {
                    System.out.println("Invalid amount");
                }
            }
            case "capitalize" -> {
                if (rest.length < 2)
                {
                    ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "USAGE:" + reset);
                    System.out.println(" ".repeat(15) + "--string" + dim + " capitalize " + reset + "[\u001B[3m\u001B[2m...message\u001B[0m]");
                    return;
                }

                String modifiedBody = "";
                for (String b : body.split(" "))
                {
                    String[] splitB = b.split("");
                    String word = "";
                    for (int i = 0; i < splitB.length; i++)
                    {
                        if (i == 0)
                        {
                            word += splitB[i].toUpperCase();
                        }
                        else
                        {
                            word += splitB[i];
                        }
                    }
                    modifiedBody += word + " ";
                }
                System.out.println(modifiedBody);
            }
            default -> {
                ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "METHODS:" + reset);
                System.out.println(" ".repeat(15) + "--string" + dim + " uppercase " + reset + "[\u001B[3m\u001B[2m...message\u001B[0m]");
                System.out.println(" ".repeat(15) + "--string" + dim + " lowercase " + reset + "[\u001B[3m\u001B[2m...message\u001B[0m]");
                System.out.println(" ".repeat(15) + "--string" + dim + " reverse " + reset + "[\u001B[3m\u001B[2m...message\u001B[0m]");
                System.out.println(" ".repeat(15) + "--string" + dim + " repeat " + reset + "[\u001B[3m\u001B[2mamount\u001B[0m]"  + " [\u001B[3m\u001B[2m...message\u001B[0m]");
                System.out.println(" ".repeat(15) + "--string" + dim + " capitalize " + reset + "[\u001B[3m\u001B[2m...message\u001B[0m]");
                return;
            }
        }
    }

    static void runSet(String[] rest) throws IOException
    {
        String dim = runSequence("dim");
        String reset = runSequence("reset");
        String yellow = runSequence("yellow");
        if (rest.length < 1)
        {
            ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "METHODS:" + reset);
            System.out.println(" ".repeat(15) + "--set" + dim + " add " + reset + "[\u001B[3m\u001B[2m...value\u001B[0m]");
            System.out.println(" ".repeat(15) + "--set" + dim + " remove " + reset + "[\u001B[3m\u001B[2m...value\u001B[0m]");
            System.out.println(" ".repeat(15) + "--set" + dim + " list " + reset);
            return;
        }

        String method = rest[0];
        HashSet<String> set = loadSet();

        switch (method)
        {
            case "add" -> {
                if (rest.length < 2)
                {
                    ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "USAGE:" + reset);
                    System.out.println(" ".repeat(15) + "--hash" + dim + " add " + reset + "[\u001B[3m\u001B[2m...values\u001B[0m]");
                    return;
                }
                String value = String.join(" ", Arrays.copyOfRange(rest, 1, rest.length));
                set.add(value);
                saveSet(set);

                System.out.println("Value has been added to set: " + value);
            }
            case "remove" -> {
                if (rest.length < 2)
                {
                    ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "USAGE:" + reset);
                    System.out.println(" ".repeat(15) + "--hash" + dim + " remove " + reset + "[\u001B[3m\u001B[2m...values\u001B[0m]");
                    return;
                }
                String value = String.join(" ", Arrays.copyOfRange(rest, 1, rest.length));

                if (value.equals("all"))
                {
                    saveSet(new HashSet<>());
                    System.out.println("All value has been removed to the set");
                    return;
                }

                set.remove(value);
                saveSet(set);

                System.out.println("Value has been removed to set: " + value);
            }
            case "list" -> set.forEach((value) -> System.out.println(value));
            default -> {
                ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "METHODS:" + reset);
                System.out.println(" ".repeat(15) + "--set" + dim + " add " + reset + "[\u001B[3m\u001B[2m...value\u001B[0m]");
                System.out.println(" ".repeat(15) + "--set" + dim + " remove " + reset + "[\u001B[3m\u001B[2m...value\u001B[0m]");
                System.out.println(" ".repeat(15) + "--set" + dim + " list " + reset);
                return;
            }
        }
    }

    static HashSet<String> loadSet() throws IOException
    {
        HashSet<String> set = new HashSet<>();
        if (Files.exists(setFile))
        {
            for (String line : Files.readAllLines(setFile))
            {
                set.add(line);
            }
        }

        return set;
    }

    static void saveSet(HashSet<String> set) throws IOException
    {
        ArrayList<String> lines = new ArrayList<>();

        for (String value : set)
        {
            lines.add(value);
        }

        Files.write(setFile, lines);
    }

    static void runHash(String[] rest) throws IOException
    {
        String dim = runSequence("dim");
        String reset = runSequence("reset");
        String yellow = runSequence("yellow");
        if (rest.length < 1)
        {
            ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "METHODS:" + reset);
            System.out.println(" ".repeat(15) + "--hash" + dim + " add " + reset + "[\u001B[3m\u001B[2mkey\u001B[0m] " + "[\u001B[3m\u001B[2m...value\u001B[0m]");
            System.out.println(" ".repeat(15) + "--hash" + dim + " remove " + reset + "[\u001B[3m\u001B[2mkey\u001B[0m]");
            System.out.println(" ".repeat(15) + "--hash" + dim + " list " + reset);
            System.out.println(" ".repeat(15) + "--hash" + dim + " get " + reset + "[\u001B[3m\u001B[2mkey\u001B[0m]");
            return;
        }
        String method = rest[0];
        HashMap<String, String> map = loadMap();

        switch (method)
        {
            case "add" -> {
                if (rest.length < 3)
                {
                    ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "USAGE:" + reset);
                    System.out.println(" ".repeat(15) + "--hash" + dim + " add " + reset + "[\u001B[3m\u001B[2mkey\u001B[0m] " + "[\u001B[3m\u001B[2m...value\u001B[0m]");
                    return;
                }
                String key = rest[1];
                String value = String.join(" ", Arrays.copyOfRange(rest, 2, rest.length));
                map.put(key, value);
                saveHash(map);
                
                System.out.println("Saved: " + key);
            }
            case "remove" -> {
                if (rest.length < 2)
                {
                    ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "USAGE:" + reset);
                    System.out.println(" ".repeat(15) + "--hash" + dim + " remove " + reset + "[\u001B[3m\u001B[2mkey\u001B[0m]");
                    return;
                }
                String key = rest[1];

                if (key.equals("all"))
                {
                    saveHash(new HashMap<>());
                    System.out.println("All hash map has been removed.");
                    return;
                }

                if (map.get(key) == null)
                {
                    System.out.println("Not found or does not exist");
                    return;
                }

                map.remove(key);
                saveHash(map);
                System.out.println("Removed: " + key);
            }
            case "get" -> {
                if (rest.length < 2)
                {
                    ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "USAGE:" + reset);
                    System.out.println(" ".repeat(15) + "--hash" + dim + " get " + reset + "[\u001B[3m\u001B[2mkey\u001B[0m]");
                    return;
                }
                String result = map.getOrDefault(rest[1], "Does not exist");
                if (result == "Does not exist")
                {
                    ErrorLog("Not Found", "");
                    return;
                }
                System.out.println(result);
            }
            case "list" -> map.forEach((key, value) -> System.out.println(key + "=" + value));
            default -> {
                ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "METHODS:" + reset);
                System.out.println(" ".repeat(15) + "--hash" + dim + " add " + reset + "[\u001B[3m\u001B[2mkey\u001B[0m] " + "[\u001B[3m\u001B[2m...value\u001B[0m]");
                System.out.println(" ".repeat(15) + "--hash" + dim + " remove " + reset + "[\u001B[3m\u001B[2mkey\u001B[0m]");
                System.out.println(" ".repeat(15) + "--hash" + dim + " list " + reset);
                System.out.println(" ".repeat(15) + "--hash" + dim + " get " + reset + "[\u001B[3m\u001B[2mkey\u001B[0m]");
                return;
            }
        }

    }

    static HashMap<String, String> loadMap() throws IOException
    {
        HashMap<String, String> map = new HashMap<>();

        if (Files.exists(mapFile))
        {
            for (String line : Files.readAllLines(mapFile))
            {
                String[] parts = line.split("=", 2);
                map.put(parts[0], parts[1]);
            }
        }

        return map;
    }

    static void saveHash(HashMap<String, String> map) throws IOException
    {
        ArrayList<String> lines = new ArrayList<>();

        for (HashMap.Entry<String, String> entry : map.entrySet())
        {
            lines.add(entry.getKey() + "=" + entry.getValue());
        }

        Files.write(mapFile, lines);
    }

    static void runAnsi(String name, String[] rest)
    {
        String sequence = runSequence(name);
        String dim = runSequence("dim");
        String reset = runSequence("reset");
        String yellow = runSequence("yellow");
        if (sequence == null)
        {
            ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "COLORS:" + reset);
            for (String i : ansiCodes.keySet())
            {
                System.out.println(" ".repeat(15) + "--ansi-" + dim + i + reset + " [\u001B[3m\u001B[2m...messages\u001B[0m]");
            }
            return;
        }

        if (rest.length < 1)
        {
            ErrorLog("The Command Is Missing Something.", "\n" + " ".repeat(10) + yellow + "USAGE:" + reset);
            System.out.println(" ".repeat(15) + "--ansi-" + name + " [\u001B[3m\u001B[2m...messages\u001B[0m]");
            return;
        }

        System.out.println(sequence + String.join(" ", rest) + runSequence("reset"));
    }

    static String runSequence(String name)
    {
        Integer code = ansiCodes.get(name);
        return code == null ? null : "\u001B[" + code + "m";
    }

}