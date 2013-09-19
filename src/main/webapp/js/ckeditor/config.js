/**
 * @license Copyright (c) 2003-2013, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.html or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';

// Toolbar configuration generated automatically by the editor based on config.toolbarGroups.
    config.toolbar = [
        { name: 'document', groups: [ 'mode', 'document', 'doctools' ], items: [ 'Source', '-', 'Templates' ] },
        { name: 'clipboard', groups: [ 'clipboard', 'undo' ], items: [ 'Cut', 'Copy', 'Paste', 'PasteText', '-', 'Undo', 'Redo' ] },
        { name: 'editing', groups: [ 'find', 'selection', 'spellchecker' ], items: [ 'Find', 'Replace', '-', 'SelectAll' ] },
        { name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ], items: [ 'Bold', 'Italic', 'Underline', '-', 'RemoveFormat' ] },
        { name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align', 'bidi' ], items: [ 'NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', '-', 'Blockquote', 'CreateDiv', '-', 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock', '-', 'BidiLtr', 'BidiRtl' ] },
        '/',
        { name: 'wcm', items: [ 'uploads' ] },
        { name: 'links', items: [ 'Link', 'Unlink' ] },
        { name: 'insert', items: [ 'Image', 'Flash', 'Table', 'HorizontalRule', 'SpecialChar', 'PageBreak'] },
        { name: 'styles', items: [ 'Styles', 'Format', 'Font', 'FontSize' ] },
        { name: 'colors', items: [ 'TextColor', 'BGColor' ] },
        { name: 'tools', items: [ 'Maximize', 'ShowBlocks' ] },
        { name: 'others', items: [ '-' ] }
    ];

    // Disable CKEditor content filter
    config.allowedContent = true;

    config.extraPlugins = 'uploads';

    config.templates_replaceContent = false;
};

// Adding these rules to templates indentation
CKEDITOR.on( 'instanceReady', function( ev )
{
    var writer = ev.editor.dataProcessor.writer;
    writer.indentationChars = '  ';
    writer.selfClosingEnd = ' />';

    var dtd = CKEDITOR.dtd;

    for ( var e in CKEDITOR.tools.extend( {'wcm-list':1, 'script':1, 'wcm-iter':1}, dtd.$block) )
    {
        ev.editor.dataProcessor.writer.setRules( e, {
            indent : true,
            breakBeforeOpen : true,
            breakAfterOpen : true,
            breakAfterClose : true,
            breakBeforeClose : true
        });
    }
    for ( var e in CKEDITOR.tools.extend( {'wcm-img':1, 'wcm-link':1, 'wcm-title':1, 'wcm-excerpt':1}, dtd.$inline ) )
    {
        ev.editor.dataProcessor.writer.setRules( e, {
            indent : true,
            breakBeforeOpen : false,
            breakAfterOpen : false,
            breakAfterClose : false,
            breakBeforeClose : false
        });
    }

});