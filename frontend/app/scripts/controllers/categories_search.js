'use strict';

/**
 * @ngdoc function
 * @name auctionHelperApp.controller:CategoriesSearchCtrl
 * @description
 * # CategoriesSearchCtrl
 * Controller of the auctionHelperApp
 */
angular.module('auctionHelperApp')
  .controller('CategoriesSearchCtrl', function (serverAddress, $scope, $http) {

    $scope.auction = {
      'name' : ''
    };

    $scope.categoryTree;

    $scope.searchForCategory = function () {
      $http.get(serverAddress + '/categories?searchPhrase=' + $scope.auction.name)
        .then(function (response) {
          $scope.categoryTree = response.data.path;

        }, function () {
          console.error("Error getting offers");
        });
    };
  });
