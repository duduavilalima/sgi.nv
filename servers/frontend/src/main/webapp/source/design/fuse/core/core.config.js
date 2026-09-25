(function ()
{
    'use strict';

    angular
        .module('app.core')
        .config(['$ariaProvider', 'msScrollConfigProvider', '$provide', 'fuseConfigProvider', function($ariaProvider, msScrollConfigProvider, $provide, fuseConfigProvider) {
                // ng-aria configuration
                $ariaProvider.config({
                    tabindex: false
                });

                // msScroll configuration
                msScrollConfigProvider.config({
                    wheelPropagation: true
                });

                // Text Angular options
                $provide.decorator('taOptions', [
                    '$delegate', function (taOptions)
                    {
                        taOptions.toolbar = [
                            ['bold', 'italics', 'underline', 'ul', 'ol', 'quote']
                        ];

                        taOptions.classes = {
                            focussed           : 'focussed',
                            toolbar            : 'ta-toolbar',
                            toolbarGroup       : 'ta-group',
                            toolbarButton      : 'md-button',
                            toolbarButtonActive: 'active',
                            disabled           : '',
                            textEditor         : 'form-control',
                            htmlEditor         : 'form-control'
                        };

                        return taOptions;
                    }
                ]);

                // Text Angular tools
                $provide.decorator('taTools', [
                    '$delegate', function (taTools)
                    {
                        taTools.bold.iconclass = 'icon-format-bold';
                        taTools.italics.iconclass = 'icon-format-italic';
                        taTools.underline.iconclass = 'icon-format-underline';
                        taTools.ul.iconclass = 'icon-format-list-bulleted';
                        taTools.ol.iconclass = 'icon-format-list-numbers';
                        taTools.quote.iconclass = 'icon-format-quote';

                        return taTools;
                    }
                ]);

                // Fuse theme configurations
                fuseConfigProvider.config({
                    'disableCustomScrollbars'        : false,
                    'disableCustomScrollbarsOnMobile': true,
                    'disableMdInkRippleOnMobile'     : true
                });

        }]);
})();