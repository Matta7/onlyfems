import { ImageType } from "../../../types/entityType"

type BigImageCardPropsType = {
  image: ImageType,
  even: boolean,
}

const BigImageCard: React.FC<BigImageCardPropsType> = ({
  image,
  even,
}) => {
  const extension = image.name.split('.').pop();
  return (
    <div className="big-image-card flex">
      { even ?
        <>
          <img loading="lazy" src={`data:image/${extension};base64, ${image.base64}`} alt={image.name} />
          <div className="description-container flex text-start">
            <p className="description">{image.description}</p>
          </div>
        </> : 
        <>
          <div className="description-container flex text-end">
            <p className="description">{image.description}</p>
          </div>
          <img loading="lazy" src={`data:image/${extension};base64, ${image.base64}`} alt={image.name} />
        </>
      }
      
    </div>
  )
}

export default BigImageCard