import dayjs from 'dayjs';
import { useState } from 'react';
import './index.scss';

type Comment = { id: number; name: string; content: string; date: Date };

interface CommentItemProps {
  comment: Comment;
  onDelete: (id: number) => void;
}
// 修改为函数组件形式
function CommentItem(props: CommentItemProps) {
  const { comment, onDelete } = props;
  return (
    <div className="comment-item">
      <div className="comment-header">
        <div className="comment-avatar">U</div>
        <div className="comment-user-info">
          <h4 className="comment-username">{comment.name}</h4>
          <p className="comment-date">{dayjs(comment.date).format('YYYY-MM-DD HH:mm:ss')}</p>
        </div>
      </div>
      <p className="comment-content">{comment.content}</p>
      <button
        className="comment-delete"
        onClick={() => onDelete(comment.id)}
      >
        删除
      </button>
    </div>
  );
}

function CommentList() {
  const [comments, setComments] = useState<Comment[]>([
    { id: 1, name: '用户1', content: '这是用户1的评论', date: new Date() },
    { id: 2, name: '用户2', content: '这是一个示例评论内容。评论内容可以很长，会自动换行显示，展示组件的美观样式。', date: new Date() },
    { id: 3, name: '用户3', content: '这是一个示例评论内容。评论内容可以很长，会自动换行显示，展示组件的美观样式。', date: new Date() },
  ]);

  const handleDeleteComment = (id: number) => {
    const newList = comments.filter((item) => item.id !== id);

    setComments(newList);
  };

  return (
    <div className="comment-list-container">
      <h2 className="comment-list-title">评论列表</h2>
      <div className="comment-list">
        {comments.map((item) => (
          <CommentItem
            key={item.id}
            comment={item}
            onDelete={handleDeleteComment}
          />
        ))}
      </div>
    </div>
  );
}

export default CommentList;
