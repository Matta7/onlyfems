import React, { useEffect } from "react";

import '../../styles/Navbar.css';
import { logoutQuery } from "../../apis/queries";
import { SnackMessageType } from "../../types/entityType";
import { Link, useNavigate } from "react-router-dom";
import { isAdmin, isAuthenticated, isVIP } from "../../utils/user";
import LoadingCircle from "../LoadingCircle";
import clientPath from "../../utils/clientPath";
import useGetUser from "../../hooks/useGetUser";

type NavbarPropsType = {
  setSnack: (smt: SnackMessageType) => void,
}

const Navbar: React.FC<NavbarPropsType> = ({
  setSnack,
}) => {
  const navigate = useNavigate()
  const { user } = useGetUser()

  useEffect(() => {}, [user])
  
  if (!user) {
    return <LoadingCircle />
  }

  const authenticated = user && isAuthenticated(user)

  const logout = async () => {
    const res = await logoutQuery();
    if (res.status === 200) {
      navigate(clientPath.HOME)
    } else {
      setSnack({
        message: 'Une erreur est survenue lors de votre déconnexion',
        type: 'error'
      })
    }
  }

  const location = window.location.pathname

  return (
    <div className="navbar flex">
      <div className="links flex">
        <Link className="title-link" to={clientPath.GALLERY}>OnlyFems</Link>
        {user ?
          <>
            {isAdmin(user) && <Link className={`link ${location.startsWith('/admin') ? 'selected' : ''}`} to={clientPath.ADMIN}>Administration</Link>}
            {isVIP(user) && <Link className={`link ${location === '/gallery/vip' ? 'selected' : ''}`} to={clientPath.VIP_GALLERY}>Section VIP</Link>}
          </> :
          <LoadingCircle />
        }
      </div>
      
      <div className="btns flex">
        {
          authenticated ?
          (
            <div className={`profile-btn btn flex ${location === '/profile' ? 'selected' : ''}`} onClick={() => navigate(clientPath.PROFILE)}>
              Profil
              <i className="fa fa-user"></i>
            </div>
          ) : 
          null
        }
        <div className="action btn" onClick={authenticated ? logout : () => navigate(clientPath.HOME)}>
          <i className={`fa fa-sign-${authenticated ? 'out' : 'in'}`}></i>
        </div>
      </div>
    </div>
  )
};

export default Navbar;