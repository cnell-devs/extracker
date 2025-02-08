import { Dropdown } from "./subComponents/Dropdown";
import { useState } from "react";
import { washCategory } from "../helpers/washCategory";

export const TransactionsList = ({ data }) => {
  const [selected, setSelected] = useState(null);
  console.log(
    new Set(data.map(({ personalFinanceCategory }) => personalFinanceCategory))
  );

  return (
    <>
      <div className="overflow-x-auto pb-10">
        <table className="table">
          {/* head */}
          <thead>
            <tr>
              <th></th>
              <th>Amount</th>
              <th>
                <Dropdown
                  dropdown={{
                    label: "Category",
                    items: ["Show All"].concat(
                      Array.from(
                        new Set(
                          data.map(
                            ({ personalFinanceCategory }) =>
                              personalFinanceCategory
                          )
                        )
                      )
                    ),
                  }}
                  setSelected={setSelected}
                />
              </th>
              {/* <th>Favorite Color</th> */}
            </tr>
          </thead>
          <tbody>
            {/* row 1 */}

            {data
              .filter(({ personalFinanceCategory }) =>
                selected
                  ? selected == "Show All"
                    ? personalFinanceCategory
                    : personalFinanceCategory == selected
                  : personalFinanceCategory
              )
              .map((entry, i) => {
                return (
                  <tr key={i}>
                    <th>{i + 1}</th>
                    <td>{entry.amount}</td>
                    <td>{washCategory(entry.personalFinanceCategory)}</td>
                    {/* <td>Blue</td> */}
                  </tr>
                );
              })}
          </tbody>
        </table>
      </div>
    </>
  );
};
