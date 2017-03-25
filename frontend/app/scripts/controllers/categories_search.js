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

    $scope.searchForCategory = function () {
      $http.get(serverAddress + '/categories?' + $scope.auction.name)
        .then(function (response) {
          $scope.categoryTree = response.data;
          console.info(response.data);
        }, function () {
          console.error("Error getting offers");
        });
    };
  });
