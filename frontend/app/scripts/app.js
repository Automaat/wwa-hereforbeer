'use strict';

/**
 * @ngdoc overview
 * @name auctionHelperApp
 * @description
 * # auctionHelperApp
 *
 * Main module of the application.
 */
var app = angular
  .module('auctionHelperApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'ui.bootstrap',
    'highcharts-ng'
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
      .when('/categories_search', {
        templateUrl: 'views/categories_search.html',
        controller: 'CategoriesSearchCtrl'
      })
      .when('/trends', {
        templateUrl: 'views/trends.html',
        controller: 'TrendsCtrl'
      })
      .when('/estimator', {
        templateUrl: 'views/estimator.html',
        controller: 'EstimatorCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  })
  .config(['$locationProvider', function($locationProvider) {
    $locationProvider.hashPrefix('');
  }]);

app.constant('serverAddress', 'http://localhost:8080');
