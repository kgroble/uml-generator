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
* Tested multiple run configurations


## Kieran
* Initial class skeletons
* Added detection and display of "implements" relations
* Parse and display class names
* Improved parsing to capture and display types of generically typed classes


## Lewis
* Wrote bare-bones class parser up to generating Graphviz code.
* Updated Javadoc comments.
* Implemented inheritance arrows and changed labels to HTML.
* Implemented access-level field/method hiding.
* Master UML maker.
