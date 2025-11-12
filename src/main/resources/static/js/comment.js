document.addEventListener("DOMContentLoaded", () => {
  const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute("content");
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute("content");

  document.querySelectorAll(".comment-add-form").forEach(form => {
    form.addEventListener("submit", e => {
      e.preventDefault();
      const postId = form.querySelector("[name='postId']").value;
      const content = form.querySelector("[name='content']").value.trim();
      if(!content) return;

      fetch("/comments/add", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
          [csrfHeader]: csrfToken
        },
        body: new URLSearchParams({ postId, content })
      })
      .then(res => res.json())
      .then(data => {
        if (data && data.username) {
          const commentHtml = `
          <div class="comment-item mt-1" data-comment-id="${data.id}">
            <strong>${data.username}</strong>:
            <span class="comment-content">${data.content}</span>
            <span>
              <button type="button" class="btn btn-sm btn-outline-danger comment-delete-btn" data-comment-id="${data.id}">削除</button>
              <button type="button" class="btn btn-sm btn-outline-primary comment-edit-btn" data-comment-id="${data.id}">編集</button>
            </span>
            <div class="comment-edit-form mt-1" data-comment-id="${data.id}" style="display:none;">
              <textarea class="form-control mb-1 comment-edit-content" required>${data.content}</textarea>
              <button type="button" class="btn btn-primary btn-sm comment-save-btn" data-comment-id="${data.id}">保存</button>
              <button type="button" class="btn btn-secondary btn-sm comment-cancel-btn" data-comment-id="${data.id}">キャンセル</button>
            </div>
          </div>`;

          form.insertAdjacentHTML("beforebegin", commentHtml);
          form.querySelector("[name='content']").value = "";
        }
      });
    });
  });

  document.addEventListener("click", e => {
    if (e.target.classList.contains("comment-delete-btn")) {
      const commentId = e.target.dataset.commentId;

      fetch("/comments/delete", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
          [csrfHeader]: csrfToken
        },
        body: new URLSearchParams({ commentId})
      })
      .then(res => res.json())
      .then(data => {
        if(data.success) {
          document.querySelector(`[data-comment-id="${commentId}"]`).remove();
        }
      });
    }
  });

  document.addEventListener("click", e => {
    if(e.target.classList.contains("comment-edit-btn")) {
      const commentId = e.target.dataset.commentId;
      const item = document.querySelector(`.comment-item[data-comment-id="${commentId}"]`);
      item.querySelector(".comment-edit-form").style.display = "block";
      item.querySelector(".comment-content").style.display = "none";
      e.target.style.display = "none";
    }
  });

  document.addEventListener("click", e => {
    if(e.target.classList.contains("comment-cancel-btn")) {
      const commentId = e.target.dataset.commentId;
      const item = document.querySelector(`.comment-item[data-comment-id="${commentId}"]`);
      item.querySelector(".comment-edit-form").style.display = "none";
      item.querySelector(".comment-content").style.display = "inline";
      item.querySelector(".comment-edit-btn").style.display = "inline";
    }
  });

  document.addEventListener("click", e =>{
    if(e.target.classList.contains("comment-save-btn")) {
      const commentId = e.target.dataset.commentId;
      const newContent = document.querySelector(`.comment-edit-form[data-comment-id="${commentId}"] .comment-edit-content`).value.trim();
      if(!newContent) return;

      fetch("/comments/edit", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
          [csrfHeader]: csrfToken
        },
        body: new URLSearchParams({ commentId, content: newContent })
      })
      .then(res => res.json())
      .then(data => {
        if(data.success) {
          const item = document.querySelector(`.comment-item[data-comment-id="${commentId}"]`);
          item.querySelector(".comment-content").textContent = newContent;
          item.querySelector(".comment-edit-form").style.display = "none";
          item.querySelector(".comment-content").style.display = "inline";
          item.querySelector(".comment-edit-btn").style.display = "inline";
        }
      });
    }
  });
});