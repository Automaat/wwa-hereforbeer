'use strict';

/**
 * @ngdoc function
 * @name auctionHelperApp.controller:EstimatorCtrl
 * @description
 * # EstimatorCtrl
 * Controller of the auctionHelperApp
 */
angular.module('auctionHelperApp')
  .controller('EstimatorCtrl', function (serverAddress, $scope, $http) {

    $scope.auction = {
      'query' : ''
    };

    $scope.estimates = {score : 10};

    $scope.estimate = function () {
      $http.get(serverAddress + '/scores?searchPhrase=' + $scope.auction.query)
        .then(function (response) {
          $scope.estimates = response.data;
          chartSpeed.series[0].points[0].update($scope.estimates.score)
          console.info(response.data);
        }, function () {
          console.error("Error getting offers");
        });
    };

    var gaugeOptions = {

      chart: {
        type: 'solidgauge'
      },

      title: null,

      pane: {
        center: ['50%', '85%'],
        size: '140%',
        startAngle: -90,
        endAngle: 90,
        background: {
          backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || '#EEE',
          innerRadius: '60%',
          outerRadius: '100%',
          shape: 'arc'
        }
      },

      tooltip: {
        enabled: false
      },

      // the value axis
      yAxis: {
        stops: [
          [0.1, '#DF5353'], // green
          [0.5, '#DDDF0D'], // yellow
          [0.8, '#55BF3B'] // red
        ],
        lineWidth: 0,
        minorTickInterval: null,
        tickAmount: 2,
        title: {
          y: -70
        },
        labels: {
          y: 16
        }
      },

      plotOptions: {
        solidgauge: {
          dataLabels: {
            y: 5,
            borderWidth: 0,
            useHTML: true
          }
        }
      }
    };

// The speed gauge
    var chartSpeed = Highcharts.chart('container-speed', Highcharts.merge(gaugeOptions, {
      yAxis: {
        min: 0,
        max: 1000,
        title: {
          text: 'Estimate'
        }
      },

      credits: {
        enabled: false
      },

      series: [{
        name: 'Speed',
        data: [$scope.estimates.score],
        dataLabels: {

        },
        tooltip: {
          valueSuffix: ' km/h'
        }
      }]

    }));

    setInterval(function () {
      // Speed
      var point,
        newVal,
        inc;

      if (chartSpeed) {
        point = chartSpeed.series[0].points[0];
        inc = Math.round((Math.random() - 0.5) * 10);
        newVal = point.y + inc;

        if (newVal < 0 || newVal > 200) {
          newVal = point.y - inc;
        }

        point.update(newVal);
      }

    }, 500);

  });
