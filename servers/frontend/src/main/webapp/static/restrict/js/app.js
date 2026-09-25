(function() {
    'use strict';

    angular.module('SGIApp', [
            'ngMaterial',
            'ngAnimate',
            'ngMessages',
            'ngCookies',
            'toastr',
            'ngMask',
            'ui.utils',
            'ui.router',
            'ngResource',
            'ui.grid',
            'ui.grid.pinning',
            'ui.grid.autoResize',
            'pxCrudHelper',
            'directives',
            'utils',

            'dataPicker',
            'md.data.table',

            //mascaras para decimais, percentagem e moeda
            'ui.utils.masks',

            'app.menu',
            'fxpicklist',

            'app.usuario',

            'app.faturamento',
            'app.etapasNaoFaturadas',
            'app.faturamentoInterfaces',
            'app.notaFiscalFaturamento',
            'app.notaDebito',
            'app.faturamentoRelatorios',
            'app.contasareceber',
            'app.notasDebitoCR',
            'app.contasareceberRelatorios',
            'app.movimentoautorizado',
            'app.movimentoautorizadoboletos',
            'app.relacaobancaria',
            'app.lancamentos',
            'app.relatorio',
            'app.conta_corrente',
            'app.banco',
            'app.historicoconciliacaobancaria',
            'app.importarconciliacao',
            'app.grafico',
            'app.regional',
            'app.fluxocaixa.relatorios',
            'app.previsao',
            'app.despesas',
            'app.tiposServico',
            'app.previsaoFaturamento',

            // Comercial
            'app.contratos',
            'app.clientes',
            'app.centrosfinanceiros',
            'app.categoriacontrato',
            'app.formacontratacao',
            'app.obras',
            'app.relacionamentocontato',
            'app.comercialRelatorios',

            // Administração
            'app.colaborador',

            // Administração
            'app.perfil',
            'app.funcionalidades',
            'app.gerencia',

            // Custos
            'app.rubricas',
            'app.grafico-resultado',
            'app.orcamento-empresarial',
            'app.comparativos',
            'app.custos.relatorios',

            // Declaração do módulo de layout (FUSE - http://withinpixels.com/themes/fuse)
            'app.core'
        ])

        .constant('$restServer', '../')

        .config(['$httpProvider', 'toastrConfig', '$mdDateLocaleProvider', '$qProvider',
            '$resourceProvider',
            function($httpProvider, toastrConfig,
                $mdDateLocaleProvider, $qProvider, $resourceProvider) {
                $qProvider.errorOnUnhandledRejections(false);

                $httpProvider.interceptors.push(function($q, $rootScope) {
                    return {
                        'request': function(config) {
                            $rootScope.$broadcast('loading-started');
                            $rootScope.loadingProgress = true;
                            return config || $q.when(config);
                        },
                        'requestError': function(rejection) {
                            $rootScope.$broadcast('loading-complete');
                            //                            $rootScope.loadingProgress = false;
                            return $q.reject(rejection);
                        },
                        'response': function(response) {
                            $rootScope.$broadcast('loading-complete');
                            $rootScope.loadingProgress = false;
                            localStorage.setItem("last_address", window.location.href);
                            return response || $q.when(response);
                        },
                        'responseError': function(rejection) {
                            $rootScope.$broadcast('loading-complete');
                            //                            $rootScope.loadingProgress = false;
                            return $q.reject(rejection);
                        },
                    };
                });

                // Evita o popup de login/senha do navegador
                $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
                $httpProvider.defaults.withCredentials = true;

                moment.locale('pt-BR');

                $mdDateLocaleProvider.months = ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'];
                $mdDateLocaleProvider.shortMonths = ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'];
                $mdDateLocaleProvider.days = ['Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado', 'Domingo'];
                $mdDateLocaleProvider.shortDays = ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sab', 'Dom'];

                $mdDateLocaleProvider.formatDate = function(date) {
                    if (angular.isDefined(date)) {
                        moment.locale('pt-BR');
                        return moment(date).format('DD/MM/YYYY');
                    }
                    return null;
                };

                $mdDateLocaleProvider.monthHeaderFormatter = function(date) {
                    return $mdDateLocaleProvider.months[date.getMonth()] + ' ' + date.getFullYear();
                };

                $mdDateLocaleProvider.parseDate = function(dateString) {
                    var m = moment(dateString, 'DD/MM/YYYY', true);
                    return m.isValid() ? m.toDate() : new Date(NaN);
                };

                angular.extend(toastrConfig, {
                    autoDismiss: false,
                    containerId: 'toast-container',
                    maxOpened: 0,
                    showMethod: 'show',
                    newestOnTop: true,
                    positionClass: 'toast-top-right',
                    preventDuplicates: false,
                    preventOpenDuplicates: true,
                    allowHtml: false,
                    closeButton: true,
                    closeHtml: '<button>&times;</button>',
                    extendedTimeOut: 20000,
                    iconClasses: {
                        error: 'toast-error',
                        info: 'toast-info',
                        success: 'toast-success',
                        warning: 'toast-warning'
                    },
                    messageClass: 'toast-message',
                    onHidden: null,
                    onShown: null,
                    onTap: null,
                    progressBar: true,
                    tapToDismiss: true,
                    timeOut: 15000,
                    titleClass: 'toast-title',
                    toastClass: 'toast'
                });

                var newActions = {
                    update: {
                        method: 'PUT'
                    },
                    get: {
                        method: 'GET'
                    },
                    page: {
                        method: 'POST',
                        params: {
                            action: 'page'
                        }
                    },
                    search: {
                        method: 'POST',
                        params: {
                            action: 'search'
                        },
                        isArray: true
                    },
                    transition: {
                        method: 'PUT',
                        params: {
                            action: 'transition'
                        }
                    }
                };

                angular.extend($resourceProvider.defaults.actions, newActions);

            }
        ])

        .run(['$rootScope', '$http', '$state', '$stateParams', '$timeout', 'AuthenticateService', '$location', '$cookies', '$transitions', function($rootScope, $http, $state, $stateParams, $timeout, AuthenticateService, $location, $cookies, $transitions) {
            // It's very handy to add references to $state and $stateParams to the $rootScope
            // so that you can access them from any scope within your applications.For example,
            // <li ui-sref-active="active }"> will set the <li> // to active whenever
            // 'contacts.list' or one of its decendents is active.
            $rootScope.$state = $state;
            $rootScope.$stateParams = $stateParams;

            let last_address = localStorage.getItem("last_address_logout");
            if (last_address) {
                localStorage.removeItem("last_address_logout");
                window.location.href = last_address;
            }

            // Activate loading indicator
            var stateChangeStartEvent = $rootScope.$on('$stateChangeStart', function() {
                AuthenticateService.checkLogin();
                $rootScope.loadingProgress = true;
            });

            // De-activate loading indicator
            var stateChangeSuccessEvent = $rootScope.$on('$stateChangeSuccess', function() {
                AuthenticateService.checkLogin();
                $timeout(function() {
                    $rootScope.loadingProgress = false;
                }, 200);
            });

            // Cleanup
            $rootScope.$on('$destroy', function() {
                stateChangeStartEvent();
                stateChangeSuccessEvent();
            });

            //TODO Breadcrumb: componentizar
            $rootScope.$breadcrumb = {
                steps: [{
                    title: 'Início',
                    link: '/'
                }],

                //TODO Guardar o $scope com o estado
                addState: function(state, params) {
                    if (!state.data) return;

                    var len = this.steps.length;
                    for (var i = 0; i < len; i++) {
                        var value = this.steps[i].title;
                        if (angular.equals(value, state.data.title)) {
                            this.steps.splice(i, 1);
                            break;
                        }
                    }

                    var link = $state.href(state, params);

                    //TODO Incluir uma propriedade identificadora no titulo (ex.: 'Cliente Fulano' no lugar de 'Consultar Cliente')
                    this.steps.push({
                        title: state.data.title,
                        link: link
                    });
                },
            };

            $transitions.onSuccess({}, function($transition) {
                $rootScope.$breadcrumb.addState($transition.to().$$state, $transition.params());
                $rootScope.$lastState = {
                    name: $transition.from().name,
                    state: $transition.from().$$state,
                    params: $transition.params()
                };
            });
        }])

        //Configuração inicial do framework de layout (FUSE - http://withinpixels.com/themes/fuse)
        .controller('AppController', ['fuseTheming', '$controller', '$scope', 'appUrl', '$cookies', '$location', function(fuseTheming, $controller, $scope, appUrl, $cookies, $location) {
            $controller('mainController', {
                $scope: $scope
            });

            $scope.getURL = function() {
                return appUrl();
            };

            var vm = this;

            // Data
            vm.themes = fuseTheming.themes;

            fuseTheming.setActiveTheme('sgi');

            vm.layoutMode = 'wide';
            var bodyEl = angular.element('body');

            // Update class on body element
            bodyEl.toggleClass('boxed', (vm.layoutMode === 'boxed'));

            $scope.initDatepicker = function() {
                angular.element(".md-datepicker-button").each(function() {
                    var el = this;
                    angular.element(this).css('visibility', 'hidden');
                });
            };
        }]);
})();
