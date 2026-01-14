import dayjs from 'dayjs';
import {nanoid} from 'nanoid';
import * as React from 'react';
import {useRef, useState} from 'react';
import './index.scss';

interface Comment {
    id: number | string;
    name: string;
    content: string;
    date: Date
}

interface CommentItemProps {
    comment: Comment;
    onDelete: (id: number | string) => void;
}

const commentList = [
    {id: 1, name: '用户1', content: '这是用户1的评论', date: new Date()},
    {
        id: 2,
        name: '用户2',
        content: '这是一个示例评论内容。评论内容可以很长，会自动换行显示，展示组件的美观样式。',
        date: new Date()
    },
    {
        id: 3,
        name: '用户3',
        content: '这是一个示例评论内容。评论内容可以很长，会自动换行显示，展示组件的美观样式。',
        date: new Date()
    },
]

// 修改为函数组件形式
function CommentItem(props: CommentItemProps) {
    const {comment, onDelete} = props;
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
    const commentInputRef = useRef<HTMLTextAreaElement>(null)

    // 评论列表
    const [comments, setComments] = useState<Comment[]>(commentList);

    // 发布评论
    const [comment, setComment] = useState<Comment>({id: 0, name: '', content: '', date: new Date()});

    /**
     * 删除评论
     * @param id 评论id
     */
    const handleDeleteComment = (id: number | string) => {
        const newList = comments.filter((item) => item.id !== id);

        setComments(newList);
    };

    /**
     * 添加评论
     */
    const handleAddComment = () => {
        if (!comment || !comment.content.trim()) return;

        setComments([...comments, comment]);
        setComment({id: 0, name: '', content: '', date: new Date()});

        // 发布完成获取焦点
        commentInputRef.current!.focus()
    };

    /**
     * 评论内容改变
     * @param e 事件
     */
    const handleCommentChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        // 通过事件获取当前评论内容
        const content1 = e.target.value.trim();
        console.log(content1)

        // 当前评论内容
        const content = commentInputRef.current!.value

        // 生成评论id
        const id = nanoid(16);
        setComment({
            id,
            name: '用户' + id,
            content,
            date: new Date(),
        });
    };

    return (
        <div className="comment-list-container h-[500px] overflow-auto">
            <h2 className="comment-list-title">评论列表</h2>

            <div className="comment-input-container mt-4 mb-6">
                <div className="relative">
          <textarea
              className="w-full h-24 p-4 text-gray-700 border-2 border-cyan-500 rounded-lg
                 focus:outline-none focus:ring-2 focus:ring-cyan-300 focus:border-transparent
                 placeholder-gray-400 resize-none transition-all duration-200
                 shadow-sm hover:shadow-md"
              placeholder="输入评论内容..."
              onChange={handleCommentChange}
              value={comment?.content}
              maxLength={500}
              ref={commentInputRef}
          />
                    <div
                        className="absolute bottom-2 right-3 text-sm text-gray-500">{comment?.content?.length || 0}/500
                    </div>
                </div>
                <div className="flex justify-end mt-2">
                    <button
                        className="px-4 py-2 bg-cyan-500 text-white rounded-lg hover:bg-cyan-600
                 focus:outline-none focus:ring-2 focus:ring-cyan-300 transition-colors
                 disabled:opacity-50 disabled:cursor-not-allowed"
                        onClick={handleAddComment}
                        disabled={!comment?.content?.trim()}
                    >
                        发布评论
                    </button>
                </div>
            </div>

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
