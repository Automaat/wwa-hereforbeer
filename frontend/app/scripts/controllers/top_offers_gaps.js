'use strict';



angular.module('auctionHelperApp')
  .controller('TopOffersGapsCtrl', function (serverAddress, $scope, $http) {

    $scope.offers = [];

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

    $scope.plotChart = function () {
      $scope.getOffers();
      $(document).ready(function () {

        // Build the chart
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
      });
    }


  });
