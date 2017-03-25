'use strict';



angular.module('auctionHelperApp')
  .controller('TopOffersGapsCtrl', function (serverAddress, $scope, $http) {

    $scope.getOffers = function (days) {
      $http.get(serverAddress + '/offer-gaps')
        .then(function (response) {
          $scope.offers = response.data;
          console.info(response.data);
        }, function () {
          console.error("Error getting offers");
        });
    };

    var charts = null;

    $scope.plotChart = function () {
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
                enabled: false
              },
              showInLegend: true
            }
          },
          series: [{
            name: 'Brands',
            colorByPoint: true,
            data: [{
              name: 'Microsoft Internet Explorer',
              y: 56.33
            }, {
              name: 'Chrome',
              y: 24.03,
              sliced: true,
              selected: true
            }, {
              name: 'Firefox',
              y: 10.38
            }, {
              name: 'Safari',
              y: 4.77
            }, {
              name: 'Opera',
              y: 0.91
            }, {
              name: 'Proprietary or Undetectable',
              y: 0.2
            }]
          }]
        });
      });
    }


  });
