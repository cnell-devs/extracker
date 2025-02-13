import { washCategory } from "../helpers/washCategory";

export const TransactionsList = ({ data, active }) => {

  return (
    <>
      <div className="overflow-x-auto pb-10">
        <table className="table">
          {/* head */}
          <thead>
            <tr>
              <th></th>
              <th>Transaction</th>
              <th>Merchant</th>
              <th>Amount</th>
              <th>
                {/* {<Dropdown
                  dropdown={{
                    label: "Primary Category",
                    items: ["SHOW_ALL"].concat(
                      Array.from(
                        new Set(
                          data.map(
                            ({ personalFinanceCategory }) =>
                              personalFinanceCategory.primary
                          )
                        )
                      )
                    ),
                  }}
                  setSelected={setSelected}
                /> } */}
                Category
              </th>
              <th>Detailed Category</th>
              <th>Date</th>
              <th>Payment Channel</th>
            </tr>
          </thead>
          <tbody>
            {/* row 1 */}

            {data
              .filter(
                  ({ personalFinanceCategory }) =>
                active != "SHOW_ALL" ?
                  personalFinanceCategory.primary == active : personalFinanceCategory
              )
              .map((entry, i) => {
                return (
                  <tr key={i}>
                    <th>{i + 1}</th>
                    <td>{entry.name}</td>
                    <td>{entry.merchantName || "---"}</td>
                    <td>{entry.amount}</td>
                    <td>
                      {washCategory(entry.personalFinanceCategory.primary)}
                    </td>
                    <td>
                      {washCategory(entry.personalFinanceCategory.detailed)}
                    </td>
                    <td>{new Date(entry.date).toLocaleDateString()}</td>
                    <td>{washCategory(entry.paymentChannel)}</td>
                  </tr>
                );
              })}
          </tbody>
        </table>
      </div>
    </>
  );
};
