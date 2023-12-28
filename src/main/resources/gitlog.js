fetch("/")
  .then((response) => response.text())
  .then((html) => {
    document.getElementById("gitLog").innerHTML = new DOMParser()
      .parseFromString(html, "text/html")
      .getElementById("gitLog").innerHTML;
  });
