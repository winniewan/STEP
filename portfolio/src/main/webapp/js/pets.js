function slideShow() {
    setTimeout(slideShow, 1000);
    const img = document.querySelectorAll(".image");
    for(var i = 0; i < img.length; i++) {
        img[i].style.display = "none";
    }

    const imgIndex = Math.floor(Math.random() * img.length);
    img[imgIndex].style.display = "block";
}

slideShow();
