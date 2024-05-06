This directory contains the demo "script".

- [`demo-script.adoc`](demo-script.adoc) is the asciidoc file containing the "script" used to go through the demo.
- [`demo-pdf-theme.yml`](demo-pdf-theme.yml) is the [Asciidoctor PDF](https://docs.asciidoctor.org/pdf-converter/latest/) theme so that when converting to PDF things line up properly on pages.

To create a pdf:
1. Install [Asciidoctor PDF](https://docs.asciidoctor.org/pdf-converter/latest/install/) (or https://docs.asciidoctor.org/asciidoctor/latest/install/macos/ for macOS)
2. From the `demo` directory run `asciidoctor-pdf --theme demo-pdf -a pdf-themesdir=. demo-script.adoc`
3. Open `demo-script.pdf`

The demo script references a number of different [IntelliJ Live Templates](https://www.jetbrains.com/help/idea/using-live-templates.html). Those templates can be found as [`intellij-live-templates/TDD_ Easier than you think.xml`](intellij-live-templates/TDD_%20Easier%20than%20you%20think.xml). You can import the templates to your own IntelliJ instance by [following the instructions](https://www.jetbrains.com/help/idea/sharing-live-templates.html#export-and-import-live-templates-manually).
