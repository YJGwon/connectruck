import React, {useState, useEffect, useContext} from 'react';
import {useNavigate} from 'react-router-dom';
import {TextField, Button} from '@mui/material';

import { UserContext } from '../../context/UserContext';

import './LoginForm.css';

export default function LoginForm({home}) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const navigate = useNavigate();
    const { isLogin, login } = useContext(UserContext);

    useEffect(() => {
        if (isLogin) {
            navigate(home);
        }
    }, [isLogin]);

    const handleFormSubmit = async (e) => {
        e.preventDefault();

        if (username.trim() === '' || password.trim() === '') {
            alert('아이디와 비밀번호를 입력하세요.');
            return;
        }

        try {
            const response = await fetch(`${process.env.REACT_APP_API_URL}/api/signin`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({username, password})
            });

            if (response.ok) {
                const data = await response.json();
                const accessToken = data.accessToken;
                // Save the access token to local storage
                login(accessToken);
            } else {
                throw new Error(`api error: ${response.json().title}`);
            }
            navigate(home);
        } catch (error) {
            console.error('Error fetching login:', error);
            if (error.message.startsWith('api error:')) {
                alert(error.message);
            } else {
                alert('로그인 하지 못하였습니다.');
            }
        }

        setUsername('');
        setPassword('');
    };

    return (
        <form className="form" onSubmit={handleFormSubmit}>
            <TextField
                className="textField"
                label="아이디"
                variant="outlined"
                value={username}
                onChange={(e) => setUsername(e.target.value.trim())}/>
            <TextField
                className="textField"
                label="비밀번호"
                variant="outlined"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value.trim())}/>
            <Button className="button" variant="contained" color="primary" type="submit">
                로그인
            </Button>
        </form>
    );
}
