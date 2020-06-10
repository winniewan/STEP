/** Creates a map and adds it to the page. */
function createMap() {
  // Map variable is assigned to a new Map object.
  // Two required options for every map: center, zoom.
  // This map centers at MTV, CA and zooms at street view.
  const map = new google.maps.Map(
      document.getElementById('map'),
      {center: {lat: 37.422, lng: -122.084}, zoom: 16});
}
