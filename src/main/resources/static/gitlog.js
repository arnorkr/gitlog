fetch("/gitlog")
  .then((response) => response.json())
  .then((data) => {
    const list = document.getElementById("gitLog");
    data.forEach((entry) => {
      const listItem = document.createElement("li");
      listItem.textContent = `${entry.message}`;
      list.appendChild(listItem);
    });
  })
  .catch((error) => console.error("Error fetching Git log:", error));
