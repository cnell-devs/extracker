// dropdown component for sorting transactions list tbale

import { washCategory } from "../../helpers/washCategory";

export const Dropdown = ({ dropdown, setSelected }) => {

  return (
    <div className="dropdown">
      <div tabIndex={0} role="button" className="btn m-1 border-0 bg-transparent hover:shadow-none">
        {dropdown.label}
      </div>
      <ul
        tabIndex={0}
        className="dropdown-content menu bg-base-100 rounded-box z-[1] w-52 p-2 shadow"
      >
        {dropdown.items.map((listItem, i) => {
          return (
            <li key={i} onClick={() => setSelected(listItem)}>
              <a>{washCategory(listItem)}</a>
            </li>
          );
        })}
      </ul>
    </div>
  );
};
