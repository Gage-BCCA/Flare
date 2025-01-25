public class UserInterface {

    final private String logo = """
                                   _                    \s
              /\\  /\\__ _ _ __   __| |___                \s
             / /_/ / _` | '_ \\ / _` / __|               \s
            / __  / (_| | | | | (_| \\__ \\               \s
            \\/ /_/ \\__,_|_| |_|\\__,_|___/               \s
                                                        \s
               ___                                      \s
              /___\\_ __                                 \s
             //  // '_ \\                                \s
            / \\_//| | | |                               \s
            \\___/ |_| |_|                               \s
                                                        \s
                             _                         _\s
              /\\ /\\___ _   _| |__   ___   __ _ _ __ __| |
             / //_/ _ \\ | | | '_ \\ / _ \\ / _` | '__/ _` |
            / __ \\  __/ |_| | |_) | (_) | (_| | | | (_| |
            \\/  \\/\\___|\\__, |_.__/ \\___/ \\__,_|_|  \\__,_|
                       |___/                            \s
            
            Track your time spent coding.
            ====================================================
            """;

    public void printLogo() {
        System.out.println(logo);
    }

    public void printMainMenu() {
        System.out.println("2 - Show Most Recent Entry");
        System.out.println("1 - Show All Entries");
        System.out.println("0 - Exit Program");
    }

    public void printEntries() {
        System.out.println("All entries");
    }

    public void printMostRecentEntry(Entry entry) {
        System.out.println(entry.language + " ---- " + entry.notes);
    }
}
