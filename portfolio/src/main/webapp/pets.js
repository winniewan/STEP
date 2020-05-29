function slideShow() {
    setTimeout(slideShow, 1000);
    const img = document.querySelectorAll(".image");
    for(var i = 0; i < 7; i++) {
        img[i].style.display = "none";
    }

    const imgIndex = Math.floor(Math.random() * 7);
    img[imgIndex].style.display = "block";
}

slideShow();
