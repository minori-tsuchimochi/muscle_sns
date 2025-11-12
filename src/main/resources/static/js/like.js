document.addEventListener("DOMContentLoaded", () => {
  const csrfToken = document.querySelector('meta[name="_csrf"]').content;
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

  document.querySelectorAll(".like-btn").forEach(button => {
    button.addEventListener("click", () => {
      const postId = button.dataset.postId;

      fetch("/likes/toggle-ajax", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
          [csrfHeader]: csrfToken
        },
        body: new URLSearchParams({ postId })
      })
      .then(res => res.json())
      .then(data =>{
        button.querySelector("span").textContent = data.liked ? "❤ いいね済み" : "💛 いいね";
        const count = button.closest(".d-flex").querySelector(".like-count");
        if(count) count.textContent = data.likeCount + " likes";
      })
      .catch(err => console.error(err));
    })
  })
})