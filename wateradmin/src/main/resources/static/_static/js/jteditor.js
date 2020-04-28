var ext_tools = ace.require("ace/ext/language_tools");

function build_editor(elm,mod){
    ace.require("ace/ext/language_tools");

    var editor = ace.edit(elm);

    editor.setTheme("ace/theme/chrome");
    editor.getSession().setMode("ace/mode/"+mod);
    editor.setOptions({
        showFoldWidgets:false,
        showLineNumbers:true,
        enableBasicAutocompletion: true,
        enableSnippets: true,
        enableLiveAutocompletion: true
    });

    editor.setHighlightActiveLine(false);
    editor.setShowPrintMargin(false);
    editor.moveCursorTo(0, 0);

    return editor;
}