/** Creates a map and adds it to the page. */
function createMap() {
  // NYC coordinates.
  var nycLatLng = new google.maps.LatLng(40.6782, -73.9442);
  var mapOptions = {
    zoom: 10,
    center: nycLatLng
  }
  
  var map = new google.maps.Map(document.getElementById("map"), mapOptions);

  var marker = new google.maps.Marker({
    animation: google.maps.Animation.DROP,
    position: nycLatLng,
    title:"Hometown!"
  });

  // To add the marker to the map, call setMap();
  marker.setMap(map);
}
