# Running instructions
To run in Eclipse:
1. Create a new run configuration (`Run -> Run Configurations...`). Select `UML-Generator` as the project and `UMLGeneratorApp` as the main class.
2. In the Arguments tab, provide (fully qualified) Java class names as the program arguments.
    * E.g., to generate UML for Java's `List` and `ArrayList` classes, enter
        `java.util.List java.util.ArrayList`
    * Note class names are delimited by spaces (no commas)
3.  There are several flags to modify behavior:
    * `-r` or `--recursive` to recursively parse and display classes discovered while parsing the provided classes
    * `--public` to render only Public classes, fields, and methods; `--protected` to render only Public and Protected classes fields, and methods; `--private` to render all classes, fields, and methods (default: `private`)
    * So to recursively display the public API of `ArrayList` and all of its ancestors,
        `-r --public java.util.ArrayList`
    * Flags can be in any order and any position. Providing more than one access level flag (`--public`, `--protected`, `--private`) yields undefined behavior.
4. Run the newly created configuration. The generated UML will be in the `./output/out.dot` file.

# Configuration File
This project features a robust framework for customizing the setup and
operations in the config file. This is, by default, located in the
config/settings.txt file. To change this, pass the command
`--settings=<filepath>` where `<filepath>` is the path to the file you
want parsed as the config file.

The format for the file is very simple, specifically in the form `var
= value`. The list of legal variables is as follows:
* `recursive`: boolean value that determines whether or not the UML
  will be generated recursively
  * Default: false
  * Example: `recursive = true`
* `synthetic`: boolean value that determines whether or not the UML
  should display synthetic methods (like lamda's)
  * Default: false
  * Example: `synthetic = true`
* `include`: list of fully qualified class names separated by spaces
  to include in the rendering
  * Default: <empty>
  * Example: `include = java.util.String java.util.ArrayList`
* `exclude`: list of package prefixes (such as `java/lang/`) to
  exclude from the rendering; this has lower priority than the
  `include` keyword, and is therefore only really useful in
  combination with the `recursive` variable
  * Default: <empty>
  * Example: `exclude = java/lang/ java/util/`
* `patterns`: list of fully qualified patterns and pattern decorators
  separated by spaces then semicolon; the format should be <<pattern>
  <patterndecorator >+ ;>*; these will be drawn in the order they're
  written, so the earliest one can be overwritten by later ones;
  patterns can take an arbitrary number of String parameters (which
  they may or may not use, see specific implementations) which are
  passed inside of parentheses separated by a comma and **NO SPACE**
  * Default: `patterns.IdentityPattern`
  * Example: `patterns = patterns.IdentityPattern;
    patterns.MalformedDecoratorPattern
    patterns.ColorDecorator(orange,red);`
* `access`: one of `public`, `private`, or `protected` to determine
  what visibility to render; this is the same as the `--public`,
  `--private`, and `--protected` command line arguments and will be
  overridden by them
  * Default: `private`
  * Example: `access = protected`
* `generate`: list of fully qualified graph generator class names
  separated by spaces to
  use to generate the graph from the class files
  * Default: `graph.SuperGraphGen graph.ImplementsGraphGen
    graph.AssociationGraphGen graph.DependencyGraphGen`
  * Example: `generate = graph.SuperGraphGen myGraphGen`
* `exporter`: single fully qualified exporter class name to be used to
  export the final generated graph; this also supports the same
  arbitrary string arguments as `patterns`
  * Default: `exporters.FileExporter`
  * Example: `exporter = exporters.FileExporter(./output/newOut.dot)`

# Team member contributions
## Joe
* Added functionality to Pattern.toGraphviz() so it recognizes fields and methods.
* Repackaged everything.
* Added Association Pattern functionality.
* Added bidirectional arrow functionality.
* Integrated dependency functionality for method return types and method bodies.
* Added cardinality labeling to single and bidirectional arrows.
* Fixed various ConfigSettings bugs.
* Added functionality for including or excluding Synthetic methods and fields.


## Kieran
* Initial class skeletons
* Added detection and display of "implements" relations
* Parse and display class names
* Improved parsing to capture and display types of generically typed classes
* Added detection of dependencies in return types and arguments
* Added drawing of dependency arrows
* Detection and rendering of Singleton patterns
* Bidirectional highlighting/project setup


## Lewis
* Wrote extensive parser up to generating Graphviz code.
* Created base style for the graph generator decorators.
* Updated Javadoc comments.
* Implemented inheritance arrows and changed labels to HTML.
* Implemented access-level field/method hiding.
* Master UML maker/curator.
* Created ConfigSettings
* Created the Inheritance Pattern
* Updated README to describe the usage of the config file
