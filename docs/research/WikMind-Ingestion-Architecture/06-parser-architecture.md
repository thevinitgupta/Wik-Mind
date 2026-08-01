# Parser Architecture

interface SourceParser {
supports();
parse();
}

Implementations:
MarkdownParser
TextParser

Future:
PdfParser
UrlParser
NotionParser

Registry:
SourceParserRegistry
