This directory contains the demo "script".

- [`demo-script-source.adoc`](demo-script-source.adoc) is the asciidoc file containing the "script" used to go through the demo. It has all kinds of include directives to include code snippets directly from the repository which don't render properly as html when viewed on GitHub.
- [`demo-script.adoc`](demo-script.adoc) is the asciidoc file containing the "script" used to go through the demo. It is reduced from [`demo-script-source.adoc`](demo-script-source.adoc) by the [`reduce-remo-script.yml`](../.github/workflows/generate-demo-script.yml) GitHub action. This is necessary because AsciiDoc preview on GitHub does not support the include directive.
- [`demo-pdf-theme.yml`](demo-pdf-theme.yml) is the [Asciidoctor PDF](https://docs.asciidoctor.org/pdf-converter/latest/) theme so that when converting to PDF things line up properly on pages.

To create a pdf:
1. Install [Asciidoctor PDF](https://docs.asciidoctor.org/pdf-converter/latest/install/) (or https://docs.asciidoctor.org/asciidoctor/latest/install/macos/ for macOS)
2. From the `demo` directory run `asciidoctor-pdf --theme demo-pdf -a pdf-themesdir=. demo-script.adoc`
3. Open `demo-script.pdf`

> [!NOTE]
> The pdf is also generated after each build by the [`reduce-remo-script.yml`](../.github/workflows/generate-demo-script.yml) GitHub action.

The demo script references a number of different [IntelliJ Live Templates](https://www.jetbrains.com/help/idea/using-live-templates.html). Those templates can be found as [`intellij-live-templates/TDD_ Easier than you think.xml`](intellij-live-templates/TDD_%20Easier%20than%20you%20think.xml). You can import the templates to your own IntelliJ instance by [following the instructions](https://www.jetbrains.com/help/idea/sharing-live-templates.html#export-and-import-live-templates-manually).