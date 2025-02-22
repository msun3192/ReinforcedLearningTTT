// Matthew Sun
// Mr. Paige
// Machine Learning
// 1/27/25
public class TTT {

    private static boolean isOption(String s) {
        if (s.length() > 1) {
            return switch (s.charAt(0)) {
                case '-', '+' -> true;
                default -> false;
            };
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        boolean first = false;
        boolean play = true;
        double rate = 0.005;      // !!! TODO !!! Probably to big
        int games = 500_000;      // !!! TODO !!! Probably to small
        int errors = 0;
        String option = "";

        for (String arg : args) {
            if (!option.isEmpty() && isOption(arg)) {
                System.err.println("Missing value for " + option);
                option = "";
                errors++;
            }
            switch (arg.toLowerCase()) {
                case "-first":
                    first = true;
                    continue;

                case "-second":
                    first = false;
                    continue;

                case "-play":
                    play = true;
                    continue;

                case "-train":
                    // Train only
                    play = false;
                    continue;

                case "-seed":
                case "-games":
                case "-rate":
                    option = arg;
                    continue;

                // Several options that you wish to implement:

                case "-reward":
                    continue;

                case "-punish":
                    continue;

                case "-dump":
                    continue;

                case "-trace":
                    continue;

                case "-verbose":
                    continue;

                case "-stats":
                case "-statistics":
                    continue;

                default:
                    if (isOption(arg)) {
                        System.err.println("Invalid option: " + arg);
                        errors++;
                        continue;
                    }
            }

            try {
                switch (option.toLowerCase()) {
                    case "-seed":
                        int seed = Integer.parseInt(arg);
                        Board.setSeed(seed);
                        break;

                    case "-games":
                        // Stop training after n games
                        games = Integer.parseInt(arg);
                        break;

                    case "-rate":
                        // Stop when training rate falls below r
                        rate = Double.parseDouble(arg);
                        break;

                    default:
                        option = "-games";
                        games = Integer.parseInt(arg);
                }

            } catch (NumberFormatException e) {
                System.err.println("Invalid value for " + option + ": " + arg);
                errors++;
            }
            option = "";
        }

        if (!option.isEmpty()) {
            System.err.println("Missing value for " + option);
            errors++;
        }

        if (errors > 0) return;
		Train.train(rate, games);
        if (play) Play.play(first);
    }
}
