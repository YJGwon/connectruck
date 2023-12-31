import React, {useState} from 'react';
import {TextField, Button} from '@mui/material';
import {useNavigate} from 'react-router-dom';

import {fetchData, fetchApi} from '../../function/CustomFetch';

import './SignupForm.css';

export default function SignupForm({root, role}) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [phone, setPhone] = useState('');
    const [isUsernameAvailable, setIsUsernameAvailable] = useState(false);
    const [isPhoneAvailable, setIsPhoneAvailable] = useState(false);

    const navigate = useNavigate();

    // 아이디 검사
    const handleUsernameCheck = async () => {
        if (username.trim() === '') {
            alert('아이디를 입력하세요.');
            return;
        }

        const url = `${process.env.REACT_APP_API_URL}/api/users/check-username`;
        const requestInfo = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({username})
        };
        const onSuccess = (data) => {
            if (data.isAvailable) {
                setIsUsernameAvailable(true);
                alert('사용 가능한 아이디입니다.');
            } else {
                setIsUsernameAvailable(false);
                alert('이미 사용 중인 아이디입니다.');
            }
        };
        fetchData({url, requestInfo}, onSuccess);
    };

    // 휴대폰 번호 검사
    const handlePhoneCheck = async () => {
        if (phone.trim() === '') {
            alert('휴대폰 번호를 입력하세요.');
            return;
        }

        const url = `${process.env.REACT_APP_API_URL}/api/users/check-phone`;
        const requestInfo = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({phone})
        };
        const onSuccess = (data) => {
            if (data.isAvailable) {
                setIsPhoneAvailable(true);
                alert('사용 가능한 번호입니다.');
            } else {
                setIsPhoneAvailable(false);
                alert('이미 사용 중인 번호입니다.');
            }
        };
        fetchData({ url, requestInfo }, onSuccess);
    };

    // 회원가입 요청
    const handleFormSubmit = async (e) => {
        e.preventDefault();

        if (username.trim() === '') {
            alert('아이디를 입력하세요.');
            return;
        }
        if (!isUsernameAvailable) {
            alert('아이디 확인이 필요합니다.');
            return;
        }
        if (password.trim() === '') {
            alert('비밀번호를 입력하세요.');
            return;
        }
        if (password.trim() !== confirmPassword.trim()) {
            alert('비밀번호가 일치하지 않습니다.');
            return;
        }
        if (phone.trim() === '') {
            alert('휴대폰 번호를 입력하세요.');
            return;
        }
        if (!isPhoneAvailable) {
            alert('휴대폰 번호 확인이 필요합니다.');
            return;
        }

        const url = `${process.env.REACT_APP_API_URL}/api/users`;
        const requestInfo = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password, phone, role })
        };
        const onSuccess = () => {
            alert('가입되었습니다.');
            navigate(`${root}/signin`);
        };
        fetchApi({url, requestInfo}, onSuccess);

        setUsername('');
        setPassword('');
        setConfirmPassword('');
        setPhone('');
    };

    // sidebar props
    const buttons = [
        {
            link: '/owners/signin',
            name: '로그인'
        }, {
            link: '/owners/signup',
            name: '회원가입',
            selected: true
        }
    ];

    return (
        <> 
            <form className="form" onSubmit={handleFormSubmit}>
                <TextField
                    className="textField"
                    label="아이디"
                    variant="outlined"
                    value={username}
                    onChange={(e) => {
                        setUsername(e.target.value);
                        setIsUsernameAvailable(false);
                    }}/>
                <Button variant="contained" color="primary" onClick={handleUsernameCheck}>
                    아이디 확인
                </Button>
                <TextField
                    className="textField"
                    label="비밀번호"
                    variant="outlined"
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}/>
                <TextField
                    className="textField"
                    label="비밀번호 확인"
                    variant="outlined"
                    type="password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}/>
                <TextField
                    className="textField"
                    label="휴대폰 번호"
                    variant="outlined"
                    value={phone}
                    onChange={(e) => {
                        setPhone(e.target.value);
                        setIsPhoneAvailable(false);
                    }}/>
                <Button variant="contained" color="primary" onClick={handlePhoneCheck}>
                    휴대폰 번호 확인
                </Button>
                <p></p>
                <Button className="button" variant="contained" color="primary" type="submit">
                    가입
                </Button>
            </form>
        </>
    );
}
