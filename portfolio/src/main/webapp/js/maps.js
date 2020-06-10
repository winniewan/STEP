/** Creates a map and adds it to the page. */
function createMap() {
  // By default, this map centers at Brooklyn, NY.
  const map = new google.maps.Map(
      document.getElementById('map'),
      {center: {lat: 40.6782, lng: -73.9442}, zoom: 10});
  
  // Marker for my hometown location: NYC.
  const NYCMarker = new google.maps.Marker({
    position: {lat: 40.6782, lng: -73.9442},
    map: map,
    title: 'Hometown: NYC'
  });
}
