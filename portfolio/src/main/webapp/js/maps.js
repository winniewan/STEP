function createMaps() {
  createNYCMap();
  createCovidMap();
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
  
  var image = 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png';
  var marker = new google.maps.Marker({
    animation: google.maps.Animation.DROP,
    position: nycLatLng,
    title: "Hometown!",
    icon: image
  });

  marker.addListener('click', function() {
    infowindow.open(map, marker);
  });

  // To add the marker to the map, call setMap();
  marker.setMap(map);
}

/** Fetches UFO sightings data from the server and displays it in a map. */
function createCovidMap() {
  fetch('/covid-data').then(response => response.json()).then((covidData) => {
    // Default center at MTV, CA.
    var LatLng = new google.maps.LatLng(35.78613674, -119.4491591);
    var mapOptions = {
      zoom: 7,
      center: LatLng
    };

    var covidMap = new google.maps.Map(document.getElementById("covidMap"), mapOptions);

    covidData.forEach((affectedArea) => {
      new google.maps.Marker(
          {position: {lat: affectedArea.lat, lng: affectedArea.lng}, map: covidMap});
    });
  });
}
