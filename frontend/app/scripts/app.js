'use strict';

/**
 * @ngdoc overview
 * @name auctionHelperApp
 * @description
 * # auctionHelperApp
 *
 * Main module of the application.
 */
angular
  .module('auctionHelperApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'ui.bootstrap'
  ])
  .config(function ($routeProvider  ) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl',
        controllerAs: 'main'
      })
      .when('/top_offers_gaps', {
        templateUrl: 'views/top_offers_gaps.html',
        controller: 'TopOffersGapsCtrl',
        controllerAs: 'topOffersGaps'
      })
      .otherwise({
        redirectTo: '/'
      });
  })
  .config(['$locationProvider', function($locationProvider) {
    $locationProvider.hashPrefix('');
  }]);
