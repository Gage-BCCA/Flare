package runtime.util;

import java.util.HashSet;

public class FlareFilter {

    private final String fileName = ".filter";

    public HashSet<String> allowedFileTypes = new HashSet<>();

    public FlareFilter() {

        // Adding file types to the HashSet
        allowedFileTypes.add("java");    // Java source code
        allowedFileTypes.add("py");      // Python script
        allowedFileTypes.add("js");      // JavaScript
        allowedFileTypes.add("cpp");     // C++ source code
        allowedFileTypes.add("c");       // C source code
        allowedFileTypes.add("cs");      // C# source code
        allowedFileTypes.add("go");      // Go source code
        allowedFileTypes.add("rb");      // Ruby script
        allowedFileTypes.add("swift");   // Swift source code
        allowedFileTypes.add("kt");      // Kotlin
        allowedFileTypes.add("html");    // HTML
        allowedFileTypes.add("css");     // CSS
        allowedFileTypes.add("json");    // JSON data
        allowedFileTypes.add("xml");     // XML
        allowedFileTypes.add("md");      // Markdown file
        allowedFileTypes.add("bash");    // Bash script
        allowedFileTypes.add("sh");      // Shell script
        allowedFileTypes.add("r");       // R language script
        allowedFileTypes.add("vhdl");    // VHDL
        allowedFileTypes.add("verilog"); // Verilog
        allowedFileTypes.add("pl");      // Perl script
        allowedFileTypes.add("clj");     // Clojure script
        allowedFileTypes.add("groovy");  // Groovy script
        allowedFileTypes.add("erl");     // Erlang script

        // Configuration and Miscellaneous Files
        allowedFileTypes.add("env");     // Environment variables
        allowedFileTypes.add("ini");     // Initialization file
        allowedFileTypes.add("conf");    // Configuration file
        allowedFileTypes.add("yaml");    // YAML configuration file
        allowedFileTypes.add("properties"); // Java properties file
        allowedFileTypes.add("toml");    // TOML configuration file

        // Database Files
        allowedFileTypes.add("db");      // Database file
        allowedFileTypes.add("sqlite");  // SQLite database
        allowedFileTypes.add("csv");     // CSV file
        allowedFileTypes.add("tsv");     // Tab-separated values
        allowedFileTypes.add("mdb");     // Microsoft Access database
        allowedFileTypes.add("sql");     // SQL file for database

        // Markup and Styling
        allowedFileTypes.add("txt");     // Plain text
        allowedFileTypes.add("rtf");     // Rich Text Format
        allowedFileTypes.add("tex");     // LaTeX document
        allowedFileTypes.add("ps");      // PostScript file
        allowedFileTypes.add("pdf");     // Portable Document Format
        allowedFileTypes.add("epub");    // ePub format

        // Web Development
        allowedFileTypes.add("jsp");     // JavaServer Pages
        allowedFileTypes.add("asp");     // Active Server Pages
        allowedFileTypes.add("php");     // PHP script
        allowedFileTypes.add("vue");     // Vue.js file
        allowedFileTypes.add("jsx");     // React JSX file
        allowedFileTypes.add("ts");      // TypeScript
        allowedFileTypes.add("scss");    // Sass stylesheet
        allowedFileTypes.add("less");    // Less stylesheet
        allowedFileTypes.add("styl");    // Stylus stylesheet

        // Testing and Documentation
        allowedFileTypes.add("test");    // Test file
        allowedFileTypes.add("spec");    // Specification file
        allowedFileTypes.add("feature"); // Cucumber feature file
        allowedFileTypes.add("log");     // Log file
        allowedFileTypes.add("trace");   // Trace file


    }
}
