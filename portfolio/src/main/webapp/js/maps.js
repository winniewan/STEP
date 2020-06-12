/** Calls all map functions. */
function createMaps() {
  createNYCMap();
  createUFOMap();

  google.charts.load('current', {
    'packages':['geochart'],
    'mapsApiKey': 'AIzaSyBkrY1XqHOusGtMlfsWSvFh5IrO75m9ZOI'
  });
  
  google.charts.setOnLoadCallback(drawRegionsMap);
}

/** Creates a map and adds it to the page. */
function createNYCMap() {
  // NYC coordinates.
  var nycLatLng = new google.maps.LatLng(40.6782, -73.9442);
  var mapOptions = {
    zoom: 10,
    center: nycLatLng
  };
  
  var map = new google.maps.Map(document.getElementById("nycMap"), mapOptions);

  var contentString = '<div id="content">'+
      '<h1>Brooklyn, NY</h1>'+
      '<p>This is where I was born and raised :)</p>'+
      '</div>';

  var infowindow = new google.maps.InfoWindow({
    content: contentString
  });
  
  // Add beach flag as styling to marker.
  var image = 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png';
  var marker = new google.maps.Marker({
    animation: google.maps.Animation.DROP,
    position: nycLatLng,
    title: "Hometown!",
    icon: image
  });
  
  // Information window appears when user clicks on marker.
  marker.addListener('click', function() {
    infowindow.open(map, marker);
  });

  // To add the marker to the map, call setMap();
  marker.setMap(map);
}

/** Fetches UFO sightings data from the server and displays it in a map. */
function createUFOMap() {
  // Default center at MTV, CA.
  var LatLng = new google.maps.LatLng(35.78613674, -119.4491591);
  var mapOptions = {
    zoom: 7,
    center: LatLng
  };
    
  var ufoMap = new google.maps.Map(document.getElementById("ufoMap"), mapOptions);

  fetch('/ufo-data')
    .then(response => response.json())
    .then((ufoData) => ufoData.forEach((sighting) => {
      new google.maps.Marker({position: {lat: sighting.lat, lng: sighting.lng}, map: ufoMap});
    }));
}

/** Display a simple hard-coded geomap using Charts API. */
function drawRegionsMap() {
  const data = new google.visualization.DataTable();
  data.addColumn('string', 'Country');
  data.addColumn('number', 'Votes');
  
  fetch('/chart')
    .then(response => response.json())
    .then((regionVotes) => Object.keys(regionVotes).forEach((region) => {
        data.addRow([region, regionVotes[region]]);

        const options = {};
        let chart = new google.visualization.GeoChart(document.getElementById('regions_div'));

        chart.draw(data, options);
    }));
}
