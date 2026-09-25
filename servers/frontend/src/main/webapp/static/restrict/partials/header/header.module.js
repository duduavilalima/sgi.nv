(function ()
{
    'use strict';

    angular
        .module('SGIApp', [])
        .config(config);

    /** @ngInject */
    function config($translatePartialLoaderProvider)
    {
        $translatePartialLoaderProvider.addPart('app/toolbar');
    }
})();
