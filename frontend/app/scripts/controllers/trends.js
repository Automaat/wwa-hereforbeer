'use strict';

/**
 * @ngdoc function
 * @name auctionHelperApp.controller:TrendsCtrl
 * @description
 * # TrendsCtrl
 * Controller of the auctionHelperApp
 */
angular.module('auctionHelperApp')
  .controller('TrendsCtrl', function (serverAddress, $scope, $http) {

    $scope.auction = {
      'query' : ''
    };
    $scope.trendsData = {
      'searches_count' : '',
      'visits_count' : '',
      'effectiveness' : ''
    };

    $scope.searchForTrends = function () {
      $http.get(serverAddress + '/categories/trends?searchPhrase=' + $scope.auction.query)
        .then(function (response) {
          $scope.trendsData = response.data;
          console.info(response.data);
        }, function () {
          console.error("Error getting offers");
        });
    };

    $scope.plotChart = function () {
      $scope.searchForTrends();

      $(document).ready(function () {
        Highcharts.chart('container', {
          chart: {
            zoomType: 'xy'
          },
          title: {
            text: 'Aggregated trends chart'
          },
          subtitle: {
            text: ''
          },
          xAxis: [{
            categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
              'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
            crosshair: true
          }],
          yAxis: [{ // Primary yAxis
            labels: {
              format: '{value}',
              style: {
                color: Highcharts.getOptions().colors[2]
              }
            },
            title: {
              text: 'Number of searches',
              style: {
                color: Highcharts.getOptions().colors[2]
              }
            },
            opposite: true

          }, { // Secondary yAxis
            gridLineWidth: 0,
            title: {
              text: 'Effectiveness',
              style: {
                color: Highcharts.getOptions().colors[0]
              }
            },
            labels: {
              format: '{value} %',
              style: {
                color: Highcharts.getOptions().colors[0]
              }
            }

          }, { // Tertiary yAxis
            gridLineWidth: 0,
            title: {
              text: 'Auctions',
              style: {
                color: Highcharts.getOptions().colors[1]
              }
            },
            labels: {
              format: '{value}',
              style: {
                color: Highcharts.getOptions().colors[1]
              }
            },
            opposite: true
          }],
          tooltip: {
            shared: true
          },
          legend: {
            layout: 'vertical',
            align: 'left',
            x: 80,
            verticalAlign: 'top',
            y: 55,
            floating: true,
            backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
          },
          series: [{
            name: 'Effectiveness',
            type: 'column',
            yAxis: 1,
            data: $scope.trendsData.effectiveness,
            tooltip: {
              valueSuffix: ' mm'
            }

          }, {
            name: 'Auctions visited',
            type: 'spline',
            yAxis: 2,
            data: $scope.trendsData.visits_count,
            marker: {
              enabled: false
            },
            dashStyle: 'shortdot',
            tooltip: {
              valueSuffix: ' mb'
            }

          }, {
            name: 'Searches count',
            type: 'spline',
            data: $scope.trendsData.searches_count,
            tooltip: {
              valueSuffix: ' °C'
            }
          }]
        });
      });
    }
  });
