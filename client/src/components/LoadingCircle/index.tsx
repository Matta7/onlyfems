import React from "react";

import '../../styles/LoadingCircle.css';

const LoadingCircle = ({
  fullscreen = false
}) => {
  return fullscreen ?
  (
    <div className="fullscreen-dimmer flex">
      <div className="loading-circle"></div>
    </div>
  ) : (
    <div className="loading-circle-container flex">
      <div className="loading-circle"></div>
    </div>
  )
}

export default LoadingCircle;