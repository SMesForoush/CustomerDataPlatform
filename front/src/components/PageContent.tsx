import Header from './Header';
import * as React from 'react';

function PageContent({ children }: any) {
  return (
    <React.Fragment>
      <Header />
      <div className="mt-10 m-0 w-full flex flex-col">{children}</div>
    </React.Fragment>
  );
}

export default PageContent;
