/** Creates a map and adds it to the page. */
function createMap() {
  // NYC coordinates
  nyc_lat = 40.6782;
  nyc_lng = -73.9442;

  // By default, this map centers at Brooklyn, NY.
  const map = new google.maps.Map(
      document.getElementById('map'),
      {center: {lat: nyc_lat, lng: nyc_lng}, zoom: 10});
  
  // Marker for my hometown location: NYC.
  const NYCMarker = new google.maps.Marker({
    position: {lat: nyc_lat, lng: nyc_lng},
    map: map,
    title: 'Hometown: NYC'
  });
}
