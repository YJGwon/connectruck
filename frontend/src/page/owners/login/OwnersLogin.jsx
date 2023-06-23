import React from "react";
import './OwnersLogin.css';

import SideBar from "../../../component/sidebar/SideBar";
import LoginForm from "../../../component/loginform/LoginForm";

export default function OwnersLogin() {
    // sidebar props
    const buttons = [
        {
            link: '/owners/signin',
            name: '로그인',
            selected: true
        },
        {
            link: '/owners/signup',
            name: '회원가입'
        }
    ];


    return (
        <>
            <SideBar buttons={buttons}/>
            <div className="content">
                <LoginForm url="/api/owners/signin" />
            </div>
        </>
    );
}