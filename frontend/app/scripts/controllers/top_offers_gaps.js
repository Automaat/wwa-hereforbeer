'use strict';



angular.module('auctionHelperApp')
  .controller('TopOffersGapsCtrl', function (serverAddress, $scope, $http) {

    $scope.offers = [];
    $scope.selectedItem = "pie";

    $scope.getOffers = function () {
      $http.get(serverAddress + '/offer-gaps')
        .then(function (response) {
          $scope.offers = response.data;
        }, function () {
          console.error("Error getting offers");
        });
    };

    var charts = null;
    $scope.getOffers();

    function Comparator(a, b) {
      if (a[1] < b[1]) return 1;
      if (a[1] > b[1]) return -1;
      return 0;
    };

    $scope.plotChart = function () {
      $scope.getOffers();
      $(document).ready(function () {

        // Build the chart
        if ($scope.selectedItem == "pie") {
          charts = Highcharts.chart('container', {
            chart: {
              plotBackgroundColor: null,
              plotBorderWidth: null,
              plotShadow: false,
              type: 'pie'
            },
            title: {
              text: 'Browser market shares January, 2015 to May, 2015'
            },
            tooltip: {
              pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
              pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                  enabled: true,
                  format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                  style: {
                    color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                  }
                },
                showInLegend: true
              }
            },
            series: [{
              name: 'Brands',
              colorByPoint: true,
              data: $scope.offers
            }]
          });
        } else {
          let output = $scope.offers.map(function(obj) {
            return Object.keys(obj).sort().map(function(key) {
              return obj[key];
            });
          });
          output = output.sort(Comparator);
          Highcharts.chart('container', {
            chart: {
              type: 'column'
            },
            title: {
              text: 'World\'s largest cities per 2014'
            },
            subtitle: {
              text: ''
            },
            xAxis: {
              type: 'category',
              labels: {
                rotation: -45,
                style: {
                  fontSize: '13px',
                  fontFamily: 'Verdana, sans-serif'
                }
              }
            },
            yAxis: {
              min: 0,
              title: {
                text: 'Percent share %'
              }
            },
            legend: {
              enabled: false
            },
            tooltip: {
              pointFormat: 'Percent share: <b>{point.y:.1f} %</b>'
            },
            series: [{
              name: '',
              data: output,
              dataLabels: {
                enabled: true,
                rotation: -90,
                color: '#FFFFFF',
                align: 'right',
                format: '{point.y:.1f}', // one decimal
                y: 10, // 10 pixels down from the top
                style: {
                  fontSize: '13px',
                  fontFamily: 'Verdana, sans-serif'
                }
              }
            }]
          });
        }

      });
    }


  });
