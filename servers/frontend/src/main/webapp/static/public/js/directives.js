angular.module('directives', [])

.directive('includeReplace', function () {
    return {
        require: 'ngInclude',
        restrict: 'A', /* optional */
        link: function (scope, el, attrs) {
            el.replaceWith(el.children());
        }
    };
})

.directive('limparComboPesquisa', function () {
    return {
        restrict: 'A', /* optional */
        link: function ($scope, el, attrs) {
            el.click(function() {
		         if (attrs !== null && attrs.value !== null) {
				   var nulo = attrs.value.valueOf("\"codigo\":null");
					if (nulo.indexOf("\"codigo\":null") != -1 || nulo.indexOf("\"codigo\":{}") != -1)
					  delete $scope.object;
//				       console.log("em branco" ,nulo);
				}
            });
        }
    };
})

.directive('myEnter', function () {
    return  {
      link:	function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if(event.which === 13) {
                scope.$apply(function (){
                    element[0].focus();
                    scope.$eval(attrs.myEnter);
                });

                event.preventDefault();
            }
        });
      }
    };
})

.directive('focusMe', function($timeout) {
  return {
    scope: { trigger: '@focusMe' },
    link: function(scope, element) {
      scope.$watch('trigger', function(value) {
        if(value === "true") {
          $timeout(function() {
            element[0].focus();
          });
        }
      });
    }
  };
})


.directive('chooseFile', function() {
    return {
      link: function (scope, elem, attrs) {
        var button = elem.find('button');
        var input = angular.element(elem[0].querySelector('input#fileInput'));
        button.bind('click', function() {
          input[0].click();
        });
        input.bind('change', function(e) {
          scope.$apply(function() {
            var files = e.target.files;
            if (files[0]) {
              scope.file = files[0];
              scope.fileName = files[0].name;
            } else {
              scope.file = null;
              scope.fileName = null;
            }
          });
        });
      }
    };
  })


.directive('fixed', fixed);

function fixed($compile, $window) {

  function postLink(scope, element, attrs) {
    var table = copyAttrs(jQLite('<table>'), element.parent());
    var clone = element.clone().removeAttr('fixed').removeAttr('ng-if');

    table.css({
      'position': 'absolute',
      'backgroundColor': '#fff',
      'zIndex': 1
    }).addClass('fixed');

    element.css('visibility', 'hidden');

    grandParent().prepend($compile(table.append(clone))(scope));

    var height = clone.prop('clientHeight');

    grandParent().css('position', 'relative').on('scroll', function () {
      if(grandParent().prop('scrollTop') === 0) {
        table.removeClass('md-whiteframe-1dp');
      } else if(!table.hasClass('md-whiteframe-1dp')) {
        table.addClass('md-whiteframe-1dp');
      }
      clone.children().css('height', grandParent().prop('scrollTop') + height + 'px');
    });

    function cells() {
      return clone.prop('rows')[0].cells.length;
    }

    function copyAttrs(newElement, oldElement) {
      var attrs = oldElement.prop('attributes');

      for(var attr in attrs) {
        newElement.attr(attrs[attr].name, attrs[attr].value);
      }

      return newElement;
    }

    function getCells(row) {
      return Array.prototype.filter.call(row.cells, function (cell) {
        return !cell.classList.contains('ng-leave');
      });
    }

    function grandParent() {
      return element.parent().parent();
    }

    function jQLite(node) {
      return angular.element(node);
    }

    function update() {
      getCells(clone.prop('rows')[0]).map(jQLite).forEach(function (copy, index) {
        if(copy.hasClass('clone')) {
          return;
        }

        copy.addClass('clone');

        var cell = element.find('th').eq(index);
        var style = $window.getComputedStyle(cell[0]);

        copy.css('paddingBottom', (height / 3) - (cell.children().prop('clientHeight') / 3) + 'px');

        var getWidth = function () {
          return style.width;
        };

        var setWidth = function () {
          copy.css('minWidth', style.width);
        };

        var listener = scope.$watch(getWidth, setWidth);

        $window.addEventListener('resize', setWidth);

        copy.on('$destroy', function () {
          listener();
          $window.removeEventListener('resize', setWidth);
        });

        cell.on('$destroy', function () {
          copy.remove();
        });
      });
    }

    scope.$watch(cells, update);

    element.on('$destroy', function () {
      table.remove();
    });
  }

  return {
    link: postLink
  };
}


