/**
 * SyntaxHighlighter
 * http://alexgorbatchev.com/SyntaxHighlighter
 *
 * SyntaxHighlighter is donationware. If you are using it, please donate.
 * http://alexgorbatchev.com/SyntaxHighlighter/donate.html
 *
 * @version
 * 3.0.83 (July 02 2010)
 *
 * @copyright
 * Copyright (C) 2004-2010 Alex Gorbatchev.
 *
 * @license
 * Dual licensed under the MIT and GPL licenses.
 */
;(function () {
    // CommonJS
    typeof(require) != 'undefined' ? SyntaxHighlighter = require('static/_static/js/shCore').SyntaxHighlighter : null;

    function Brush() {
        var funcs = 'abs avg case cast coalesce convert count current_timestamp ' +
            'current_user day isnull left lower month nullif replace right ' +
            'session_user space substring sum system_user upper user year';

        var keywords = 'absolute action add after alter as asc at authorization begin bigint ' +
            'binary bit by cascade char character check checkpoint close collate ' +
            'column commit committed connect connection constraint contains continue ' +
            'create cube current current_date current_time cursor database date ' +
            'deallocate dec decimal declare default delete desc distinct double drop ' +
            'dynamic else end end-exec escape except exec execute false fetch first ' +
            'float force foreign forward free from full global goto grant ' +
            'group grouping having hour ignore index inner insensitive insert instead ' +
            'int integer intersect into is isolation key last level load local max min ' +
            'minute modify move name national nchar next no numeric of off on only ' +
            'open option order out output partial password precision prepare primary ' +
            'prior privileges procedure public read real references relative repeatable ' +
            'restrict return returns revoke rollback rollup rows rule schema scroll ' +
            'second section select sequence serializable set size smallint static ' +
            'statistics table temp temporary then time timestamp to top transaction ' +
            'translation trigger true truncate uncommitted union unique update values ' +
            'varchar varying view when where with work all and any between cross in join ' +
            'like not null or outer some _for_';


        var keywords2 = 'BREAK CASE CATCH CONTINUE ' +
            'DEFAULT DO ELSE FALSE  ' +
            ' FUNCTION IF IN INSTANCEOF ' +
            'NEW NULL RETURN SUPER SWITCH ' +
            'THIS THROW TRUE TRY TYPEOF VAR WHILE WITH ' +
            'break case catch continue ' +
            'default delete do else false  ' +
            'for function if in instanceof ' +
            'new null return super switch ' +
            'this throw true try typeof var while with';


        this.regexList = [
            {regex: /--(.*)$/gm, css: 'comments'},			// one line and multiline comments
            {regex: /\b\?|&lt;|&gt;|\:|\=|\-|\+|\*|for\b/gmi, css: 'jsWord'},			//symbol
            {regex: new RegExp(this.getKeywords(keywords), 'gmi'), css: 'keyword'},			// keyword
            {regex: new RegExp(this.getKeywords(keywords2), 'gm'), css: 'jsWord'},			// keyword2
            {regex: /\b(?:(?!(if|for|this|case|while|try|catch|\s))[\w+])+\b(?=\()/gmi, css: 'functions'},			//functions
            {regex: /\b([\d]+(\.[\d]+)?|0x[a-f0-9]+)\b/gi, css: 'keyword'},			// numbers
            {regex: SyntaxHighlighter.regexLib.multiLineDoubleQuotedString, css: 'string'},			// double quoted strings
            {regex: SyntaxHighlighter.regexLib.multiLineSingleQuotedString, css: 'string'},			// single quoted strings
            {regex: new RegExp(this.getKeywords(funcs), 'gmi'), css: 'functions'},			// functions
            {regex: /\/\*(.*?)\*\//gmi, css: 'color1'}				//  匹配/**/中内容。
        ];
    };

    Brush.prototype = new SyntaxHighlighter.Highlighter();
    Brush.aliases = ['sql'];

    SyntaxHighlighter.brushes.Sql = Brush;

    // CommonJS
    typeof(exports) != 'undefined' ? exports.Brush = Brush : null;
})();

