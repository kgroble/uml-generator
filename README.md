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


## Lewis
* Wrote extensive parser up to generating Graphviz code.
* Created base style for the graph generator decorators.
* Updated Javadoc comments.
* Implemented inheritance arrows and changed labels to HTML.
* Implemented access-level field/method hiding.
* Master UML maker/curator.
