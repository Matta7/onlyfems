import React, { useEffect, useRef } from "react";
import { SnackMessageType } from "../../types/entityType";

import '../../styles/SnackMessage.css';

type SnackMessageTypeProps = {
  snackMessage: SnackMessageType,
  fullTop?: boolean,
  closeAction: () => void
}

const SnackMessage: React.FC<SnackMessageTypeProps> = ({
  snackMessage,
  fullTop = false,
  closeAction
}) => {
  const snackRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    snackRef.current?.animate(
      [
        { opacity: 1.0 },
        { opacity: 0.0 }
      ],
      {
        duration: 5000,
        fill: 'forwards',
        easing: 'ease-in'
      }
    )
    setTimeout(closeAction, 5000);
  }, [closeAction])

  return (
    <div 
      className={`snack-message flex ${snackMessage.type} ${fullTop ? 'full-top' : ''}`}
      ref={snackRef}
    >
      <p>{snackMessage.message}</p>
      <div className="btn-container">
        <div className="close-btn" onClick={closeAction}>
          <i className="fa fa-times"></i>
        </div>
      </div>
    </div>
  );
}

export default SnackMessage;