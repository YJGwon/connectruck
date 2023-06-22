import React, {useState} from 'react';
import {TextField, Button} from '@mui/material';

import './LoginForm.css';

export default function LoginForm({url}) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleFormSubmit = (e) => {
        e.preventDefault();
        console.log('url:', `${process.env.REACT_APP_API_URL}${url}`);
        console.log('id:', username);
        console.log('password:', password);
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
                onChange={(e) => setUsername(e.target.value)}/>
            <TextField
                className="textField"
                label="비밀번호"
                variant="outlined"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}/>
            <Button className="button" variant="contained" color="primary" type="submit">
                로그인
            </Button>
        </form>
    );
}
