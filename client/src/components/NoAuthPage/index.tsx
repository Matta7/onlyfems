import React, { useState } from "react";

import '../../styles/NoAuthPage.css';
import LoginForm from "./LoginForm";
import RegisterForm from "./RegisterForm";
import LoadingCircle from "../LoadingCircle";
import { SnackMessageType } from "../../types/entityType";
import { useNavigate } from "react-router-dom";
import clientPath from "../../utils/clientPath";
import { isAuthenticated } from "../../utils/user";
import useGetUser from "../../hooks/useGetUser";
import SnackMessage from "../SnackMessage";

type NoAuthPagePropsType = {
  setSnack: (smt: SnackMessageType | null) => void,
  snackMessage: SnackMessageType | null,
}

const NoAuthPage: React.FC<NoAuthPagePropsType> = ({
  setSnack,
  snackMessage,
}) => {
  const [displayLogin, setDisplayLogin] = useState(true);
  const [displayRegister, setDisplayRegister] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const navigate = useNavigate();
  const { user } = useGetUser()
  
  if (!user) {
    return <LoadingCircle fullscreen />
  }

  // Redirects to gallery to avoid multiple login
  if (isAuthenticated(user)) {
    navigate(clientPath.GALLERY)
  }

  const setLoginTrue = () => {
    setDisplayLogin(true);
    setDisplayRegister(false);
  }

  const setRegisterTrue = () => {
    setDisplayRegister(true);
    setDisplayLogin(false);
  }

  const setLoadingState = (state: boolean): void => {
    setIsLoading(state);
  }

  return (
    <div className="no-auth-page flex">
      { isLoading ? <LoadingCircle fullscreen /> : null }
      { snackMessage ? <SnackMessage snackMessage={snackMessage} fullTop closeAction={() => setSnack(null)} /> : null }
      <div className="presentation flex">
        <h1 className="title">OnlyFems</h1>
        <p className="abstract">Un portfolio de ZINZIN <span className="u-cant-c-me">(chokbar de bz)</span></p>
        <div className="buttons-group flex">
          <button onClick={setLoginTrue}>Connexion</button>
          <button onClick={setRegisterTrue}>Inscription</button>
          <button onClick={() => navigate(clientPath.GALLERY)}>Accéder sans s'authentifier</button>
        </div>
      </div>
      <div className="forms flex">
        {
          displayLogin ?
          <LoginForm setIsLoading={setLoadingState} /> :
          null
        }
        {
          displayRegister ?
          <RegisterForm 
            removeRegisterCallback={() => setDisplayRegister(false)}
            putLoginCallback={() => setDisplayLogin(true)}
            setIsLoading={setLoadingState} 
            setSnack={setSnack} 
          /> :
          null
        }
      </div>
    </div>
  );
};

export default NoAuthPage;