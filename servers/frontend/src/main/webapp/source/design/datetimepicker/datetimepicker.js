angular.module('dataPicker', [])
    .directive('ttDatePicker', ['$timeout', function($timeout) {
        return {
            require: 'ngModel',
            restrict: 'A',
            scope : {
                locale : "=",
                maxDate: "=maxDate",
                minDate: "=minDate",
                ngModel: '='
            },
            controller: function($element, $attrs, $scope) {
                $scope.$watch('ngModel', function(newValue, oldValue) {
                    if (angular.isUndefined(newValue) || newValue == null) {
                        $($element[0]).datetimepicker({
                            onShow:function( ct ){
                                this.setOptions({})
                            }
                        });
                        $element.val('');
                    }
                });
                $scope.$watch('locale', function(newValue, oldValue) {
					var options = {};
					if (newValue) {
						options.lang = newValue.replace("_","-").replace("-US","");
					} else {
						options.lang = 'pt-BR';
					}
					$($element[0]).datetimepicker({
						onShow:function( ct ){
							this.setOptions(options)
						}
					});
				});
                $scope.$watch('maxDate', function(newValue, oldValue) {
                    if (angular.isDefined(newValue) && angular.isDate(newValue)) {
                        var options = {};
                        if (angular.isDefined($scope.maxDate) && angular.isDate($scope.maxDate)) {
                            options.maxDate = new Date($scope.maxDate);
                        }
                        $($element[0]).datetimepicker({
                            onShow:function( ct ){
                                this.setOptions(options)
                            }
                        });
                    }
                });
                $scope.$watch('minDate', function(newValue, oldValue) {
                    if (angular.isDefined(newValue) && angular.isDate(newValue)) {
                        var options = {};
                        if (angular.isDefined($scope.minDate) && angular.isDate($scope.minDate)) {
                            options.minDate = new Date($scope.minDate);
                        }
                        $($element[0]).datetimepicker({
                            onShow:function( ct ){
                                this.setOptions(options)
                            }
                        });
                    }
                });
            },
            link: function (scope, element, attrs, ngModel) {
                scope.$watch(attrs.ngModel, function(newValue, oldValue) {
                  if(angular.isDefined(newValue) || newValue != null) {
                      ngModel.$setViewValue(moment(element.val(), "DD/MM/YYYY").hour(12).toDate());
                  } else {
                      ngModel.$setViewValue('');
                  }
                }, true);
                ngModel.$render = function() {
                    if (angular.isDefined(ngModel.$modelValue) || ngModel.$modelValue != null) {
                        element.val(moment.utc(ngModel.$modelValue).utcOffset("Z").format('DD/MM/YYYY'));
                    }
                };
                $timeout(function() {

                    var options = {};

                    if (angular.isDefined(scope.maxDate) && angular.isDate(scope.maxDate)) {
                        options.maxDate = new Date(scope.maxDate);
                    }
                    if (angular.isDefined(scope.minDate) && angular.isDate(scope.minDate)) {
                        options.minDate = new Date(scope.minDate);
                    }

                    if (scope.locale) {
						options.lang = scope.locale.trim().replace("_","-").replace("-US","");
					} else {
						options.lang = "pt-BR";
					}

                    $(element[0]).datetimepicker({
                        timepicker:false,
                        scrollMonth: false,
                        onClose: function () {
                            ngModel.$parsers.unshift(function(viewValue) {
                                if (angular.isUndefined(viewValue) || viewValue === '' || viewValue === ' ' || viewValue === 'Invalid Date' || viewValue == null) {
                                    return '';
                                } else {
                                    if (angular.isDate(viewValue)) {
                                        return viewValue;
                                    } else {
                                        if (viewValue != null && viewValue.length == 10) {
                                            return moment(viewValue, "DD/MM/YYYY").hour(12).toDate();
                                        }
                                        return null;
                                    }
                                }
                            });
                        },
                        scrollInput: false,
                        lang: options.lang,
                        mask: '39/19/9999',
                        format:'d/m/Y',
                        onShow:function( ct ){
                            this.setOptions(options)
                        },
                        onChangeDateTime:function(dp,$input){
                            if (angular.isUndefined($input.val()) || $input.val() == '' || $input.val() == ' ' || $input.val() == null) {
                                this.setOptions({})
                            }
                        },
                        mask:false,
                        allowBlank: true,
                        closeOnDateSelect: true

                    })
                    .blur(function (e) {
                        if (!angular.isDate(ngModel.$modelValue) && angular.isDefined(ngModel.$modelValue) && ngModel.$modelValue != null && ngModel.$modelValue !== '' && ngModel.$modelValue !== ' ') {
							var m = ngModel.$modelValue.match("(((0[1-9]|[12][0-9]|3[01])([/])(0[13578]|10|12)([/])(\\d{4}))|(([0][1-9]|[12][0-9]|30)([/])(0[469]|11)([/])(\d{4}))|((0[1-9]|1[0-9]|2[0-8])([/])(02)([/])(\d{4}))|((29)(\.|-|\/)(02)([/])([02468][048]00))|((29)([/])(02)([/])([13579][26]00))|((29)([/])(02)([/])([0-9][0-9][0][48]))|((29)([/])(02)([/])([0-9][0-9][2468][048]))|((29)([/])(02)([/])([0-9][0-9][13579][26])))");
							if (m) {
								ngModel.$setViewValue(moment(ngModel.$modelValue, 'DD/MM/YYYY', true).format());
								$(element[0]).val(m[0]);
							} else {
							    $(element[0]).val('');
							}
                        }
                    })
                    .keyup(function (e) {
                        if (e.keyCode == 8 || e.keyCode == 46) {
//                            $(element[0]).val("");
//                            ngModel.$setViewValue(null);
                        }
                    });
                });
            }
        }
    }])
    .directive('ttTimePicker', ['$timeout', function($timeout) {
        return {
            require: 'ngModel',
            restrict: 'A',
            scope : {
             localeCode: "=",
                maxDate: "=maxDate",
                minDate: "=minDate",
                ngModel: '='
            },
            controller: function($element, $attrs, $scope) {
            	$scope.$watch('localeCode', function(newValue, oldValue) {
					if (angular.isDefined(newValue)) {
						var options = {};
						if (newValue) {
							options.lang = newValue;
						} else {
							options.lang = 'pt-BR';
						}
						$($element[0]).datetimepicker({
							onShow:function( ct ){
								this.setOptions(options)
							}
						});
					}
				});
                $scope.$watch('maxDate', function(newValue, oldValue) {
                    if (angular.isDefined(newValue) && angular.isDate(newValue)) {
                        var options = {};
                        if (angular.isDefined($scope.maxDate) && angular.isDate($scope.maxDate)) {
                            options.maxDate = new Date($scope.maxDate);
                        }
                        $($element[0]).datetimepicker({
                            onShow:function( ct ){
                                this.setOptions(options)
                            }
                        });
                    }
                });
                $scope.$watch('minDate', function(newValue, oldValue) {
                    if (angular.isDefined(newValue) && angular.isDate(newValue)) {
                        var options = {};
                        if (angular.isDefined($scope.minDate) && angular.isDate($scope.minDate)) {
                            options.minDate = new Date($scope.minDate);
                        }
                        $($element[0]).datetimepicker({
                            onShow:function( ct ){
                                this.setOptions(options)
                            }
                        });
                    }
                });
            },
            link: function (scope, element, attrs, ngModel) {
                scope.$watch(attrs.ngModel, function(newValue, oldValue) {
                  if(angular.isDefined(newValue) || newValue != null) {
                      ngModel.$setViewValue(moment(element.val(), "DD/MM/YYYY").toDate());
                  } else {
                      ngModel.$setViewValue('');
                  }
                }, true);
                ngModel.$render = function() {
                    if (angular.isDefined(ngModel.$modelValue) || ngModel.$modelValue != null) {
                        element.val(moment(ngModel.$modelValue).format('DD/MM/YYYY'));
                    }
                };
                $timeout(function() {

                    var options = {};

                    if (angular.isDefined(scope.maxDate) && angular.isDate(scope.maxDate)) {
                        options.maxDate = new Date(scope.maxDate);
                    }
                    if (angular.isDefined(scope.minDate) && angular.isDate(scope.minDate)) {
                        options.minDate = new Date(scope.minDate);
                    }

                    if (scope.localeCode) {
						options.lang = scope.localeCode;
					} else {
						options.lang = 'pt-BR';
					}

                    $(element[0]).datetimepicker({
                        datepicker:false,
                        scrollTime : false,
                        onClose: function () {
                            ngModel.$parsers.unshift(function(viewValue) {
                                if (angular.isUndefined(viewValue) || viewValue === '' || viewValue === ' ' || viewValue === 'Invalid Date' || viewValue == null) {
                                    return null;
                                } else {
                                    if (angular.isDate(viewValue)) {
                                        return viewValue;
                                    } else {
                                        return moment(viewValue, "DD/MM/YYYY").toDate();
                                    }
                                }
                            });
                        },
                        mask: '29:59',
                        format:'H:i',
                        onShow:function( ct ){
                            this.setOptions(options)
                        },
                        allowBlank: true
                    }).keyup(function (e) {
                            if (e.keyCode == 8 || e.keyCode == 46) {
                                $(element[0]).val("");
                            }
                    });
                });
            }
        }
    }])
    .directive('ttDateTimePicker', ['$timeout', function($timeout) {
        return {
            require: 'ngModel',
            restrict: 'A',
            scope : {
             localeCode: "=",
                maxDate: "=maxDate",
                minDate: "=minDate",
                ngModel: '='
            },
            controller: function($element, $attrs, $scope) {
            	$scope.$watch('localeCode', function(newValue, oldValue) {
					if (angular.isDefined(newValue)) {
						var options = {};
						if (newValue) {
							options.lang = newValue;
						} else {
							options.lang = 'pt-BR';
						}
						$($element[0]).datetimepicker({
							onShow:function( ct ){
								this.setOptions(options)
							}
						});
					}
				});
                $scope.$watch('maxDate', function(newValue, oldValue) {
                    if (angular.isDefined(newValue) && angular.isDate(newValue)) {
                        var options = {};
                        if (angular.isDefined($scope.maxDate) && angular.isDate($scope.maxDate)) {
                            options.maxDate = new Date($scope.maxDate);
                        }
                        $($element[0]).datetimepicker({
                            onShow:function( ct ){
                                this.setOptions(options)
                            }
                        });
                    }
                });
                $scope.$watch('minDate', function(newValue, oldValue) {
                    if (angular.isDefined(newValue) && angular.isDate(newValue)) {
                        var options = {};
                        if (angular.isDefined($scope.minDate) && angular.isDate($scope.minDate)) {
                            options.minDate = new Date($scope.minDate);
                        }
                        $($element[0]).datetimepicker({
                            onShow:function( ct ){
                                this.setOptions(options)
                            }
                        });
                    }
                });
            },
            link: function(scope, element, attrs, ngModel) {
                scope.$watch(attrs.ngModel, function(newValue, oldValue) {
                  if(angular.isDefined(newValue) || newValue != null) {
                      ngModel.$setViewValue(moment(element.val(), "DD/MM/YYYY").toDate());
                  } else {
                      ngModel.$setViewValue('');
                  }
                }, true);
                ngModel.$render = function() {
                    if (angular.isDefined(ngModel.$modelValue) || ngModel.$modelValue != null) {
                        element.val(moment(ngModel.$modelValue).format('DD/MM/YYYY'));
                    }
                };
                $timeout(function() {

                    var options = {};

                    if (angular.isDefined(scope.maxDate) && angular.isDate(scope.maxDate)) {
                        options.maxDate = new Date(scope.maxDate);
                    }
                    if (angular.isDefined(scope.minDate) && angular.isDate(scope.minDate)) {
                        options.minDate = new Date(scope.minDate);
                    }

                    if (scope.localeCode) {
						options.lang = scope.localeCode;
					} else {
						options.lang = 'pt-BR';
					}

                    $(element[0]).datetimepicker({
                        scrollMonth: false,
                        scrollTime : false,
                        allowBlank: true,
                        mask: '39/19/9999',
                        format:'d/m/Y H:i',
                        onShow:function( ct ){
                            this.setOptions(options)
                        },
                        mask:false,
                        onClose: function () {
                            ngModel.$parsers.unshift(function(viewValue) {
                                if (angular.isUndefined(viewValue) || viewValue === '' || viewValue === ' ' || viewValue === 'Invalid Date' || viewValue == null) {
                                    return null;
                                } else {
                                    if (angular.isDate(viewValue)) {
                                        return viewValue;
                                    } else {
                                        return moment(viewValue, "DD/MM/YYYY").toDate();
                                    }
                                }
                            });
                        }
                    });
                });
            }
        }
    }]);
